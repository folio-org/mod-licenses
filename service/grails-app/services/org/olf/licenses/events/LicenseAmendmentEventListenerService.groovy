package org.olf.licenses.events

import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.grails.datastore.mapping.engine.event.PreDeleteEvent
import org.olf.licenses.LicenseAmendment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener

/**
 * Publishes CREATE and DELETE events for {@link LicenseAmendment} via GORM
 * persistence-event listeners.
 *
 * {@code ApplicationListener} is intentionally generic + instanceof-filtered —
 * narrowing the generic parameter is unreliable under Spring's type-erasure
 * dispatch.
 */
@Slf4j
class LicenseAmendmentEventListenerService implements ApplicationListener<ApplicationEvent> {

    @Autowired LicenseAmendmentEventService licenseAmendmentEventService

    @Override
    void onApplicationEvent(ApplicationEvent event) {
        if (!(event instanceof AbstractPersistenceEvent)) return
        if (!(event.entityObject instanceof LicenseAmendment)) return

        LicenseAmendment amendment = (LicenseAmendment) event.entityObject

        if (event instanceof PostInsertEvent) {
            licenseAmendmentEventService.publishCreate(amendment)
        } else if (event instanceof PreDeleteEvent) {
            licenseAmendmentEventService.publishDelete(amendment)
        }
    }
}