package org.olf.licenses.events

import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Stepwise

@Slf4j
@Integration
@Stepwise
class LicenseUpdateEventSpec extends LicenseEventBaseSpec {

    void 'PUT /licenses/licenses/{id} emits UPDATE event with pre- and post- snapshots'() {
        given: 'a freshly created license'
            String originalName = "kafka-update-${System.currentTimeMillis()}".toString()
            Map postPayload = [
                name     : originalName,
                status   : 'In negotiation',
                type     : 'Local',
                startDate: '2019-01-01'
            ]
            def created = doPost('/licenses/licenses', postPayload)
            created?.id != null

        when: 'we PUT changes to name and status'
            String newName = "${originalName}-renamed".toString()
            Map putPayload = [
                name  : newName,
                status: 'Active'
            ]
            def updated = doPut("/licenses/licenses/${created.id}", putPayload)

        then: 'PUT succeeded'
            updated?.id == created.id
            updated.name == newName
            updated.status?.value == 'active'

        when: 'we consume the UPDATE event for this license'
            String topic = topicFor('license')
            Map event = pollForUpdateEvent(topic, created.id as String)

        then: 'envelope shape is UPDATE with both old and new snapshots'
            event != null
            event.type == 'UPDATE'
            event.tenant == expectedEventTenant
            event.eventId != null
            event.eventTs != null
            event.old != null
            event.new != null

        and: 'ids match on both sides'
            event.old.id == created.id
            event.new.id == created.id

        and: 'name diff observable (also guards same-instance-mutation gotcha)'
            event.old.name == originalName
            event.new.name == newName

        and: 'status label diff observable via status.value'
            // RefdataValue.value is the *normalised* form: lower-cased with
            // spaces collapsed to underscores. The POST sent 'In negotiation'.
            event.old.status?.value == 'in_negotiation'
            event.new.status?.value == 'active'

        and: 'amendments remain ID-ref lists on both sides'
            event.old.amendments instanceof List
            event.new.amendments instanceof List
    }

    // Regression: pre-snapshot walking lazy collections in the outer session
    // used to leave dirty state that conflicted with super.update()'s save,
    // surfacing as StaleStateException at flush time when the license had
    // populated collections. The trigger is any PUT on such a license — the
    // mutating operation itself need not touch the populated collections.
    // A scalar-only PUT is the minimal reproduction.
    void 'PUT /licenses/licenses/{id} succeeds when the license has populated collections'() {
        given: 'a license with a populated alternateNames collection'
            String originalName = "kafka-update-collections-${System.currentTimeMillis()}".toString()
            Map postPayload = [
                name          : originalName,
                status        : 'Active',
                type          : 'Local',
                startDate     : '2019-01-01',
                alternateNames: [[name: 'AliasOne'], [name: 'AliasTwo']]
            ]
            def created = doPost('/licenses/licenses', postPayload)
            created?.id != null
            (created.alternateNames as List)?.size() == 2

        when: 'we PUT a scalar-only change (does not touch the collection)'
            def updated = null
            Exception caught = null
            try {
                updated = doPut("/licenses/licenses/${created.id}", [description: 'Updated description'])
            } catch (Exception e) {
                caught = e
            }

        then: 'the PUT completes without a StaleStateException / optimistic-locking failure'
            caught == null
            updated?.id == created.id
            updated.description == 'Updated description'
            // Scalar-only PUT preserves the existing collection unchanged
            (updated.alternateNames as List)?.size() == 2

        when: 'we consume the UPDATE event'
            String topic = topicFor('license')
            Map event = pollForUpdateEvent(topic, created.id as String)

        then: 'a valid UPDATE event landed with the populated collection on both snapshots'
            event != null
            event.type == 'UPDATE'
            (event.old?.alternateNames as List)?.size() == 2
            (event.new?.alternateNames as List)?.size() == 2
            event.new.description == 'Updated description'
            event.old.description != 'Updated description'
    }

    void 'PUT /licenses/licenses/{id} with invalid payload produces no UPDATE event'() {
        given: 'a freshly created license'
            String name = "kafka-update-bad-${System.currentTimeMillis()}".toString()
            Map postPayload = [
                name     : name,
                status   : 'Active',
                type     : 'Local',
                startDate: '2019-01-01'
            ]
            def created = doPost('/licenses/licenses', postPayload)

        when: 'we snapshot the topic timestamp then PUT an invalid change (blank name)'
            String topic = topicFor('license')
            long snapshotTs = System.currentTimeMillis()
            boolean caught = false
            try {
                doPut("/licenses/licenses/${created.id}", [name: ''])
            } catch (Exception e) {
                caught = true
            }

        then: 'either the request errored or we drain the topic for a bit'
            caught || true

        when: 'we drain the topic for a few seconds'
            List<Map> events = drainEvents(topic)

        then: 'no UPDATE event for our license appeared after the snapshot'
            events.findAll {
                (it.eventTs as long) >= snapshotTs && it.type == 'UPDATE' && it.new?.id == created.id
            }.isEmpty()
    }

    private Map pollForUpdateEvent(String topic, String licenseId, long timeoutMs = 15_000L) {
        pollForEvent(topic, timeoutMs) { it.type == 'UPDATE' && it.new?.id == licenseId }
    }
}

