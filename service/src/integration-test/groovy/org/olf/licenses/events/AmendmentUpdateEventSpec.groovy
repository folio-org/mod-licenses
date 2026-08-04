package org.olf.licenses.events

import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Stepwise

/**
 * UPDATE events for LicenseAmendment.
 */
@Slf4j
@Integration
@Stepwise
class AmendmentUpdateEventSpec extends LicenseEventBaseSpec {

    void 'nested PUT /licenses/licenses/{id} emits amendment UPDATE with old and new'() {
        given: 'a license carrying one amendment named A'
            String licenseName = "kafka-am-update-${System.currentTimeMillis()}".toString()
            def license = doPost('/licenses/licenses', [
                name     : licenseName,
                status   : 'Active',
                type     : 'Local',
                startDate: '2019-01-01'
            ])
            String originalName = "amendment-A-${System.currentTimeMillis()}".toString()
            def withAmendment = doPut("/licenses/licenses/${license.id}", [
                amendments: [[
                    name     : originalName,
                    status   : 'Active',
                    startDate: '2020-01-01'
                ]]
            ])
            String amendmentId = (withAmendment.amendments as List)[0].id
            amendmentId != null

        when: 'we rename the amendment to B through the parent license'
            String newName = "${originalName}-B".toString()
            def updated = doPut("/licenses/licenses/${license.id}", [
                amendments: [[
                    id  : amendmentId,
                    name: newName
                ]]
            ])

        then: 'the rename took effect'
            (updated.amendments as List).find { it.id == amendmentId }?.name == newName

        when: 'we consume the UPDATE event off the amendment topic'
            String topic = topicFor('amendment')
            Map event = pollForEvent(topic) { it.type == 'UPDATE' && it.new?.id == amendmentId }

        then: 'envelope is UPDATE with both sides populated'
            event != null
            event.tenant == expectedEventTenant
            event.old != null
            event.new != null

        and: 'the same amendment on both sides'
            event.old.id == amendmentId
            event.new.id == amendmentId

        and: 'the name diff is observable — also guards the same-instance-mutation gotcha'
            event.old.name == originalName
            event.new.name == newName

        and: 'the owner linkage survives the update'
            event.old.owner?.id == license.id
            event.new.owner?.id == license.id
    }

    void 'PUT touching only the license emits no amendment UPDATE'() {
        given: 'a license carrying one amendment'
            String licenseName = "kafka-am-noop-${System.currentTimeMillis()}".toString()
            def license = doPost('/licenses/licenses', [
                name     : licenseName,
                status   : 'Active',
                type     : 'Local',
                startDate: '2019-01-01'
            ])
            def withAmendment = doPut("/licenses/licenses/${license.id}", [
                amendments: [[
                    name     : "amendment-untouched-${System.currentTimeMillis()}".toString(),
                    status   : 'Active',
                    startDate: '2020-01-01'
                ]]
            ])
            String amendmentId = (withAmendment.amendments as List)[0].id

        when: 'we snapshot the topic then change a license scalar only'
            String topic = topicFor('amendment')
            long snapshotTs = System.currentTimeMillis()
            doPut("/licenses/licenses/${license.id}", [description: 'license-only edit'])

        and: 'we drain the amendment topic'
            List<Map> events = drainEvents(topic)

        then: 'the untouched amendment produced no UPDATE — the changed-only guard held'
            events.findAll {
                (it.eventTs as long) >= snapshotTs &&
                it.type == 'UPDATE' &&
                it.new?.id == amendmentId
            }.isEmpty()
    }

    void 'PUT /licenses/amendments/{id} emits no UPDATE event'() {
        given: 'a license carrying one amendment'
            String licenseName = "kafka-am-direct-put-${System.currentTimeMillis()}".toString()
            def license = doPost('/licenses/licenses', [
                name     : licenseName,
                status   : 'Active',
                type     : 'Local',
                startDate: '2019-01-01'
            ])
            def withAmendment = doPut("/licenses/licenses/${license.id}", [
                amendments: [[
                    name     : "amendment-direct-${System.currentTimeMillis()}".toString(),
                    status   : 'Active',
                    startDate: '2020-01-01'
                ]]
            ])
            String amendmentId = (withAmendment.amendments as List)[0].id

        when: 'we snapshot the topic then PUT the amendment endpoint directly'
            String topic = topicFor('amendment')
            long snapshotTs = System.currentTimeMillis()
            String directName = "renamed-directly-${System.currentTimeMillis()}".toString()
            def updated = doPut("/licenses/amendments/${amendmentId}", [name: directName])

        then: 'the rename itself succeeded — only the event is suppressed'
            updated?.name == directName

        when: 'we drain the amendment topic'
            List<Map> events = drainEvents(topic)

        then: 'no UPDATE event was published for it'
            events.findAll {
                (it.eventTs as long) >= snapshotTs &&
                it.type == 'UPDATE' &&
                it.new?.id == amendmentId
            }.isEmpty()
    }
}