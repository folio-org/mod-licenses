package org.olf.licenses.events

import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Stepwise

@Slf4j
@Integration
@Stepwise
class LicenseCreateEventSpec extends LicenseEventBaseSpec {

    void 'POST /licenses/licenses emits CREATE event carrying full new snapshot and no old field'() {
        given: 'a valid license payload'
            String licenseName = "kafka-create-${System.currentTimeMillis()}".toString()
            Map payload = [
                name     : licenseName,
                status   : 'Active',
                type     : 'Local',
                startDate: '2019-01-01'
            ]

        when: 'we POST to /licenses/licenses'
            def response = doPost('/licenses/licenses', payload)

        then: 'the license was created'
            response != null
            response.id != null

        when: 'we consume from the tenant-scoped topic'
            String topic = topicFor('license')
            Map event = pollForCreateEvent(topic, response.id as String, 15_000L)

        then: 'a CREATE envelope for this license landed with only the new snapshot'
            event != null
            event.type == 'CREATE'
            event.tenant == expectedEventTenant
            event.eventId != null
            event.eventTs != null
            !event.containsKey('old')
            event.new != null
            event.new.id == response.id
            event.new.name == licenseName
            event.new.status?.value == 'active'
            event.new.amendments instanceof List
            event.new.orgs instanceof List
            event.new.dateCreated ==~ /\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}Z/
            event.new.lastUpdated ==~ /\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}Z/
            event.new.startDate ==~ /\d{4}-\d{2}-\d{2}/
    }

    // The CREATE hook is a GORM PostInsertEvent listener, so LicenseSnapshotBuilder
    // walks the collection graph *during flush*. That is the riskiest path in the
    // whole feature and a scalar-only POST never exercises it: this is the case
    // where a lazy-initialisation or flush-ordering problem would surface.
    //
    // Deliberately no assertions on nested ids: cascaded children have not been
    // inserted yet when the parent's PostInsertEvent fires, so their uuid2 ids may
    // still be null. Content (names, values, flags) comes from the bound in-memory
    // graph and is what consumers actually need.
    void 'POST with populated collections emits CREATE event carrying the whole graph'() {
        given: 'a license payload with every locally-owned collection populated'
            String licenseName = "kafka-create-collections-${System.currentTimeMillis()}".toString()
            Map payload = [
                name          : licenseName,
                status        : 'Active',
                type          : 'Local',
                startDate     : '2019-01-01',
                alternateNames: [[name: 'AliasOne'], [name: 'AliasTwo']],
                orgs          : [
                    [
                        primaryOrg: true,
                        note      : 'The primary licensor',
                        roles     : [[role: 'Licensor']],
                        org       : [name: 'EBSCO', orgsUuid: '1234-5678-9102-3356']
                    ],
                    [
                        org: [name: 'Some Content Provider', orgsUuid: '1234-5678-9102-3355']
                    ]
                ],
                contacts      : [[user: 'kafka-test-user', role: 'Subject specialist']],
                docs          : [[
                    name    : 'Kafka test document',
                    location: 'http://a.b.c/d/e/f.doc',
                    url     : 'http://a.b.c/d',
                    note    : 'This is a document attachment',
                    atType  : 'Product data sheet'
                ]],
                tags          : ['kafka-create-tag']
            ]

        when: 'we POST it'
            def response = doPost('/licenses/licenses', payload)

        then: 'the license was created with its collections'
            response != null
            response.id != null
            (response.alternateNames as List)?.size() == 2
            (response.orgs as List)?.size() == 2

        when: 'we consume the CREATE event'
            String topic = topicFor('license')
            Map event = pollForCreateEvent(topic, response.id as String, 15_000L)

        then: 'the event arrived at all — walking the graph mid-flush did not break the insert'
            event != null
            event.new.name == licenseName

        and: 'alternateNames are fully serialized'
            (event.new.alternateNames as List)?.size() == 2
            (event.new.alternateNames as List).collect { it.name }.toSorted() == ['AliasOne', 'AliasTwo']

        and: 'orgs carry the primaryOrg marker, the note and the nested org wrapper'
            (event.new.orgs as List)?.size() == 2
            Map primary = (event.new.orgs as List).find { it.primaryOrg == true } as Map
            primary != null
            primary.note == 'The primary licensor'
            primary.org?.name == 'EBSCO'
            primary.org?.orgsUuid == '1234-5678-9102-3356'
            primary.roles instanceof List
            (event.new.orgs as List).find { it.org?.name == 'Some Content Provider' } != null

        and: 'contacts, docs and tags are present'
            (event.new.contacts as List)?.size() == 1
            (event.new.contacts as List)[0].user == 'kafka-test-user'
            (event.new.docs as List)?.size() == 1
            (event.new.docs as List)[0].name == 'Kafka test document'
            (event.new.docs as List)[0].url == 'http://a.b.c/d'
            (event.new.tags as List)?.size() == 1

        and: 'fileUpload is never expanded — the event path must not touch S3'
            (event.new.docs as List).every { !((Map) it).containsKey('fileUpload') }

        and: 'customProperties stay excluded'
            !event.new.containsKey('customProperties')
    }

    void 'POST /licenses/licenses with missing name produces no event for that request'() {
        given: 'we snapshot the current highest eventTs on the topic'
            String topic = topicFor('license')
            long snapshotTs = System.currentTimeMillis()

        when: 'we POST an invalid payload (no name)'
            Map badPayload = [
                status: 'Active',
                type  : 'Local'
            ]
            boolean caught = false
            try {
                doPost('/licenses/licenses', badPayload)
            } catch (Exception e) {
                caught = true
            }

        then: 'the request failed'
            caught

        when: 'we drain the topic for a few seconds'
            List<Map> events = pollForEvents(topic, Integer.MAX_VALUE, 6_000L)

        then: 'no event newer than our snapshot slipped onto the topic'
            events.findAll { (it.eventTs as long) >= snapshotTs }.isEmpty()
    }

    // mod-licenses-specific: LicenseAmendment extends LicenseCore, not License,
    // so the PostInsertEvent listener's `instanceof License` guard must not
    // fire for amendments.
    //
    // The assertion has to be made against the AMENDMENT's id: were the guard
    // broken, the stray CREATE event would carry the amendment id, not the
    // parent license id. Checking the parent id here would pass either way.
    void 'Creating a LicenseAmendment emits no License CREATE event'() {
        given: 'a license to amend'
            String licenseName = "kafka-create-amendment-parent-${System.currentTimeMillis()}".toString()
            def created = doPost('/licenses/licenses', [
                name     : licenseName,
                status   : 'Active',
                type     : 'Local',
                startDate: '2019-01-01'
            ])

        and: 'the parent exists'
            created?.id != null

        when: 'we snapshot the topic timestamp then add a nested amendment via PUT'
            String topic = topicFor('license')
            long snapshotTs = System.currentTimeMillis()
            def updated = doPut("/licenses/licenses/${created.id}", [
                amendments: [[
                    name     : "kafka-amendment-${System.currentTimeMillis()}".toString(),
                    status   : 'Active',
                    startDate: '2020-01-01'
                ]]
            ])

        then: 'the amendment was persisted and we know its id'
            (updated.amendments as List)?.size() == 1
            String amendmentId = (updated.amendments as List)[0].id
            amendmentId != null

        when: 'we drain the topic'
            List<Map> events = pollForEvents(topic, Integer.MAX_VALUE, 6_000L)
            List<Map> newEvents = events.findAll { (it.eventTs as long) >= snapshotTs }

        then: 'positive control — the PUT itself did produce an UPDATE event for the parent'
            // Without this the negative assertion below could pass simply because
            // nothing at all reached the broker.
            newEvents.any { it.type == 'UPDATE' && it.new?.id == created.id }

        and: 'no CREATE event was emitted for the amendment'
            newEvents.findAll { it.type == 'CREATE' && it.new?.id == amendmentId }.isEmpty()

        and: 'the amendment shows up on the parent as an id-ref only'
            Map updateEvent = newEvents.find { it.type == 'UPDATE' && it.new?.id == created.id }
            (updateEvent.new.amendments as List).any { it.id == amendmentId }
            (updateEvent.new.amendments as List).every { ((Map) it).keySet() == ['id'] as Set }
    }

    private Map pollForCreateEvent(String topic, String licenseId, long timeoutMs) {
        long deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            List<Map> events = pollForEvents(topic, Integer.MAX_VALUE, 6_000L)
            Map match = events.find { it.type == 'CREATE' && it.new?.id == licenseId }
            if (match != null) return match
        }
        return null
    }
}