package org.olf.licenses.events

import groovy.util.logging.Slf4j
import org.olf.licenses.License
import org.olf.licenses.LicenseAmendment
import org.springframework.beans.factory.annotation.Autowired

/**
 * Single seam for {@link LicenseAmendment} domain events — snapshot capture,
 * envelope construction, tenant resolution, topic naming, post-commit publish.
 * Sibling of {@link LicenseEventService}, publishing to its own topic so
 * consumers can subscribe to amendments granularly.
 *
 * Callers MUST invoke from within an active tx — the publish methods register
 * a post-commit sync on that tx (see {@link EventPublisherService}).
 */
@Slf4j
class LicenseAmendmentEventService {

    private static final String ENTITY = 'amendment'

    @Autowired EventPublisherService eventPublisherService
    @Autowired TopicNameResolver     topicNameResolver
    @Autowired TenantContext         tenantContext

    /**
     * Snapshot the amendment then {@code discard()} it so any dirty session
     * state left by walking lazy collections cannot participate in a
     * subsequent flush.
     */
    Map<String, Object> captureSnapshotAndDiscard(LicenseAmendment amendment) {
        if (amendment == null) return null
        Map<String, Object> snapshot = LicenseAmendmentSnapshotBuilder.snapshot(amendment)
        amendment.discard()
        return snapshot
    }

    /**
     * Snapshot every amendment hanging off a license, keyed by amendment id,
     * for the before/after diff in {@code LicenseController.update()}.
     *
     * MUST run before the license itself is discarded — discarding evicts it
     * from the session and its collections stop being walkable.
     */
    Map<String, Map> captureAmendmentSnapshots(License license) {
        if (license == null) return [:]
        Map<String, Map> out = [:]
        try {
            license.amendments?.each { LicenseAmendment amendment ->
                Map<String, Object> snapshot = captureSnapshotAndDiscard(amendment)
                if (snapshot != null) out.put((String) snapshot.id, snapshot)
            }
        } catch (Exception e) {
            log.error("Failed to capture amendment snapshots for License ${license?.id}", e)
        }
        return out
    }

    void publishCreate(LicenseAmendment amendment) {
        if (amendment == null) return
        try {
            // No discard() here — this runs mid-flush from the PostInsertEvent
            // listener, where evicting the entity being inserted is not safe.
            Map<String, Object> snapshot = LicenseAmendmentSnapshotBuilder.snapshot(amendment)
            DomainEvent<Map> event = DomainEvent.createEvent(snapshot, tenantContext.currentTenant())
            eventPublisherService.publishAfterCommit(topicNameResolver.topicFor(ENTITY), event)
        } catch (Exception e) {
            log.error("Failed to enqueue CREATE event for LicenseAmendment ${amendment?.id}", e)
        }
    }

    /**
     * Publish a DELETE carrying the lean pre-delete projection.
     *
     * Called from the PreDeleteEvent listener while the row still exists, so
     * the projection is read off the already-loaded entity — no extra SELECT.
     * No {@code discard()}: evicting an entity Hibernate is in the middle of
     * deleting is not safe, exactly as on the CREATE path.
     */
    void publishDelete(LicenseAmendment amendment) {
        if (amendment == null) return
        try {
            Map<String, Object> projection = LicenseAmendmentSnapshotBuilder.deleteProjection(amendment)
            DomainEvent<Map> event = DomainEvent.deleteEvent(projection, tenantContext.currentTenant())
            eventPublisherService.publishAfterCommit(topicNameResolver.topicFor(ENTITY), event)
        } catch (Exception e) {
            log.error("Failed to enqueue DELETE event for LicenseAmendment ${amendment?.id}", e)
        }
    }

    void publishUpdate(Map<String, Object> oldSnapshot, Map<String, Object> newSnapshot) {
        if (oldSnapshot == null || newSnapshot == null) return
        try {
            DomainEvent<Map> event = DomainEvent.updateEvent(oldSnapshot, newSnapshot, tenantContext.currentTenant())
            eventPublisherService.publishAfterCommit(topicNameResolver.topicFor(ENTITY), event)
        } catch (Exception e) {
            log.error("Failed to enqueue UPDATE event for LicenseAmendment id=${newSnapshot?.id}", e)
        }
    }

    /**
     * Diff two {@code captureAmendmentSnapshots} results and publish an UPDATE
     * per amendment that actually changed.
     */
    void publishUpdates(Map<String, Map> oldSnapshots, Map<String, Map> newSnapshots) {
        if (!oldSnapshots || !newSnapshots) return
        newSnapshots.each { String id, Map newSnapshot ->
            Map oldSnapshot = oldSnapshots.get(id)
            if (oldSnapshot == null) return
            if (withoutLastUpdated(oldSnapshot) == withoutLastUpdated(newSnapshot)) return
            publishUpdate((Map<String, Object>) oldSnapshot, (Map<String, Object>) newSnapshot)
        }
    }

    /**
     * {@code lastUpdated} is excluded from the changed-only comparison — but
     * kept in the published payload — exactly as on the License path in
     * {@code LicenseController.update()}.
     */
    private static Map withoutLastUpdated(Map snapshot) {
        snapshot.findAll { entry -> entry.key != 'lastUpdated' }
    }
}