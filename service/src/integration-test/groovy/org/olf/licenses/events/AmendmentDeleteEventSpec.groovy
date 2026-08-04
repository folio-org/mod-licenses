package org.olf.licenses.events

import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Stepwise

/**
 * DELETE events for LicenseAmendment.
 */
@Slf4j
@Integration
@Stepwise
class AmendmentDeleteEventSpec extends LicenseEventBaseSpec {

    void 'nested PUT with _delete emits amendment DELETE carrying the pre-delete projection'() {
        given: 'a license carrying one amendment'
            String licenseName = "kafka-am-delete-nested-${System.currentTimeMillis()}".toString()
            def license = doPost('/licenses/licenses', [
                name     : licenseName,
                status   : 'Active',
                type     : 'Local',
                startDate: '2019-01-01'
            ])
            String amendmentName = "amendment-to-delete-${System.currentTimeMillis()}".toString()
            def withAmendment = doPut("/licenses/licenses/${license.id}", [
                amendments: [[
                    name       : amendmentName,
                    description: 'doomed',
                    status     : 'Active',
                    startDate  : '2020-01-01'
                ]]
            ])
            String amendmentId = (withAmendment.amendments as List)[0].id
            amendmentId != null

        when: 'we remove it the way ui-licenses does'
            def updated = doPut("/licenses/licenses/${license.id}", [
                amendments: [[
                    id      : amendmentId,
                    _delete : true
                ]]
            ])

        then: 'the amendment is gone from the license'
            (updated.amendments as List).find { it.id == amendmentId } == null

        when: 'we consume the DELETE event'
            String topic = topicFor('amendment')
            Map event = pollForEvent(topic) { it.type == 'DELETE' && it.old?.id == amendmentId }

        then: 'envelope is DELETE with old only'
            event != null
            event.tenant == expectedEventTenant
            event.eventId != null
            event.old != null

        and: 'no new field is present on a delete'
            !event.containsKey('new')

        and: 'the projection identifies what was removed and from where'
            event.old.id == amendmentId
            event.old.name == amendmentName
            event.old.description == 'doomed'
            event.old.owner?.id == license.id

        and: 'refdata keeps the same object shape as CREATE and UPDATE carry'
            event.old.status instanceof Map
            event.old.status?.value == 'active'

        and: 'the lean projection omits the hasMany collections'
            !event.old.containsKey('docs')
            !event.old.containsKey('supplementaryDocs')
            !event.old.containsKey('contacts')
            !event.old.containsKey('tags')
            !event.old.containsKey('links')
            !event.old.containsKey('customProperties')
    }

    void 'DELETE /licenses/amendments/{id} emits amendment DELETE'() {
        given: 'a license carrying one amendment'
            String licenseName = "kafka-am-delete-direct-${System.currentTimeMillis()}".toString()
            def license = doPost('/licenses/licenses', [
                name     : licenseName,
                status   : 'Active',
                type     : 'Local',
                startDate: '2019-01-01'
            ])
            String amendmentName = "amendment-direct-delete-${System.currentTimeMillis()}".toString()
            def withAmendment = doPut("/licenses/licenses/${license.id}", [
                amendments: [[
                    name     : amendmentName,
                    status   : 'Active',
                    startDate: '2020-01-01'
                ]]
            ])
            String amendmentId = (withAmendment.amendments as List)[0].id

        when: 'we delete through the amendment endpoint'
            doDelete("/licenses/amendments/${amendmentId}")

        and: 'we consume the DELETE event'
            String topic = topicFor('amendment')
            Map event = pollForEvent(topic) { it.type == 'DELETE' && it.old?.id == amendmentId }

        then: 'the listener fired even though no controller override exists'
            event != null
            event.old.name == amendmentName
            event.old.owner?.id == license.id
            !event.containsKey('new')
    }

}