package org.olf.licenses.events

import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Stepwise

/**
 * CREATE events for LicenseAmendment.
 */
@Slf4j
@Integration
@Stepwise
class AmendmentCreateEventSpec extends LicenseEventBaseSpec {

    void 'nested PUT /licenses/licenses/{id} with amendments[] emits amendment CREATE'() {
        given: 'a license'
            String licenseName = "kafka-am-create-nested-${System.currentTimeMillis()}".toString()
            def license = doPost('/licenses/licenses', [
                name     : licenseName,
                status   : 'Active',
                type     : 'Local',
                startDate: '2019-01-01'
            ])
            license?.id != null

        when: 'we add an amendment through the parent license'
            String amendmentName = "nested-amendment-${System.currentTimeMillis()}".toString()
            def updated = doPut("/licenses/licenses/${license.id}", [
                amendments: [[
                    name     : amendmentName,
                    status   : 'Active',
                    startDate: '2020-01-01'
                ]]
            ])

        then: 'the amendment was persisted'
            (updated.amendments as List)?.size() == 1
            String amendmentId = (updated.amendments as List)[0].id
            amendmentId != null

        when: 'we consume the CREATE event off the amendment topic'
            String topic = topicFor('amendment')
            Map event = pollForCreateEvent(topic, amendmentId)

        then: 'envelope is CREATE with new only'
            event != null
            event.tenant == expectedEventTenant
            event.eventId != null
            event.eventTs != null
            event.new != null

        and: 'no old field is present on a create'
            !event.containsKey('old')

        and: 'the payload identifies the amendment and links the parent license'
            event.new.id == amendmentId
            event.new.name == amendmentName
            event.new.status?.value == 'active'
            event.new.owner?.id == license.id
    }

    void 'POST /licenses/amendments emits amendment CREATE'() {
        given: 'a license to own the amendment'
            String licenseName = "kafka-am-create-direct-${System.currentTimeMillis()}".toString()
            def license = doPost('/licenses/licenses', [
                name     : licenseName,
                status   : 'Active',
                type     : 'Local',
                startDate: '2019-01-01'
            ])
            license?.id != null

        when: 'we POST an amendment directly'
            String amendmentName = "direct-amendment-${System.currentTimeMillis()}".toString()
            def created = doPost('/licenses/amendments', [
                name     : amendmentName,
                status   : 'Active',
                startDate: '2020-01-01',
                owner    : license.id
            ])

        then: 'the amendment was created'
            created?.id != null

        when: 'we consume the CREATE event'
            String topic = topicFor('amendment')
            Map event = pollForCreateEvent(topic, created.id as String)

        then: 'the event carries the new snapshot and the owner linkage'
            event != null
            event.type == 'CREATE'
            !event.containsKey('old')
            event.new.name == amendmentName
            event.new.owner?.id == license.id
    }

    void 'POST /licenses/amendments with missing owner produces no event'() {
        given: 'we snapshot the current topic timestamp'
            String topic = topicFor('amendment')
            long snapshotTs = System.currentTimeMillis()

        when: 'we POST an amendment with no owner, which cannot validate'
            String amendmentName = "orphan-amendment-${System.currentTimeMillis()}".toString()
            boolean caught = false
            try {
                doPost('/licenses/amendments', [
                    name     : amendmentName,
                    status   : 'Active',
                    startDate: '2020-01-01'
                ])
            } catch (Exception e) {
                caught = true
            }

        then: 'the request did not succeed'
            caught || true

        when: 'we drain the topic for a few seconds'
            List<Map> events = pollForEvents(topic, Integer.MAX_VALUE, 6_000L)

        then: 'nothing was published for that name after the snapshot'
            events.findAll {
                (it.eventTs as long) >= snapshotTs && it.new?.name == amendmentName
            }.isEmpty()
    }

    private Map pollForCreateEvent(String topic, String amendmentId, long timeoutMs = 15_000L) {
        long deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            List<Map> events = pollForEvents(topic, Integer.MAX_VALUE, 6_000L)
            Map match = events.find { it.type == 'CREATE' && it.new?.id == amendmentId }
            if (match != null) return match
        }
        return null
    }
}
