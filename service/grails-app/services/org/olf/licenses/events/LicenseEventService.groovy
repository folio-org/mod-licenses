package org.olf.licenses.events

import groovy.util.logging.Slf4j
import org.olf.licenses.License
import org.springframework.beans.factory.annotation.Autowired

/**
 * Single seam for {@link License} domain events — snapshot capture, envelope
 * construction, tenant resolution, topic naming, post-commit publish.
 * Consolidates the CREATE path (called from
 * {@code LicenseEventListenerService}) and the UPDATE path (called from
 * {@code LicenseController}).
 *
 * Callers MUST invoke from within an active tx — the publish methods
 * register a post-commit sync on that tx (see {@link EventPublisherService}).
 */
@Slf4j
class LicenseEventService {

    private static final String ENTITY = 'license'

    @Autowired EventPublisherService eventPublisherService
    @Autowired TopicNameResolver     topicNameResolver
    @Autowired TenantContext         tenantContext

    /**
     * Snapshot the license then {@code discard()} it so any dirty session
     * state left by walking lazy collections cannot participate in a
     * subsequent flush.
     */
    Map<String, Object> captureSnapshotAndDiscard(License license) {
        if (license == null) return null
        Map<String, Object> snapshot = LicenseSnapshotBuilder.snapshot(license)
        license.discard()
        return snapshot
    }

    void publishCreate(License license) {
        if (license == null) return
        try {
            Map<String, Object> snapshot = LicenseSnapshotBuilder.snapshot(license)
            DomainEvent<Map> event = DomainEvent.createEvent(snapshot, tenantContext.currentTenant())
            eventPublisherService.publishAfterCommit(topicNameResolver.topicFor(ENTITY), event)
        } catch (Exception e) {
            log.error("Failed to enqueue CREATE event for License ${license?.id}", e)
        }
    }

    void publishUpdate(Map<String, Object> oldSnapshot, Map<String, Object> newSnapshot) {
        if (oldSnapshot == null || newSnapshot == null) return
        try {
            DomainEvent<Map> event = DomainEvent.updateEvent(oldSnapshot, newSnapshot, tenantContext.currentTenant())
            eventPublisherService.publishAfterCommit(topicNameResolver.topicFor(ENTITY), event)
        } catch (Exception e) {
            log.error("Failed to enqueue UPDATE event for License id=${newSnapshot?.id}", e)
        }
    }
}

