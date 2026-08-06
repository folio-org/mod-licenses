package org.olf.licenses.events

import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.PostInsertEvent
import org.olf.licenses.License
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener

/**
 * Publishes CREATE events for {@link License} via a GORM
 * PostInsertEvent listener. Covers all save paths (REST, clones, background
 * jobs) without touching {@code AccessPolicyAwareController.save()}.
 *
 * {@code ApplicationListener} is intentionally generic + instanceof-filtered —
 * narrowing to {@code PostInsertEvent} is unreliable under Spring's
 * type-erasure dispatch.
 */
@Slf4j
class LicenseEventListenerService implements ApplicationListener<ApplicationEvent> {

    @Autowired LicenseEventService licenseEventService

    @Override
    void onApplicationEvent(ApplicationEvent event) {
        if (!(event instanceof AbstractPersistenceEvent)) return
        if (!(event instanceof PostInsertEvent)) return
        if (!(event.entityObject instanceof License)) return

        licenseEventService.publishCreate((License) event.entityObject)
    }
}

