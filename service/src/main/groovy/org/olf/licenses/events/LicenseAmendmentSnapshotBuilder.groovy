package org.olf.licenses.events

import java.time.temporal.ChronoUnit

import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.tags.Tag
import groovy.transform.CompileStatic
import org.olf.general.DocumentAttachment
import org.olf.licenses.InternalContact
import org.olf.licenses.LicenseAmendment
import org.olf.licenses.LicenseLink

/**
 * Builds the JSON-ready Map payload for a {@link LicenseAmendment} domain event.
 *
 * Must be invoked while the Hibernate session is active so lazy collections
 * resolve rather than throwing later.
 */
@CompileStatic
class LicenseAmendmentSnapshotBuilder {

    static Map<String, Object> snapshot(LicenseAmendment amendment) {
        if (amendment == null) return null

        Map<String, Object> out = [:]
        out.id                  = amendment.id
        out.name                = amendment.name
        out.description         = amendment.description
        out.dateCreated         = asString(amendment.dateCreated)
        out.lastUpdated         = asString(amendment.lastUpdated)
        out.startDate           = asString(amendment.startDate)
        out.endDate             = asString(amendment.endDate)
        out.openEnded           = amendment.openEnded

        out.status              = refdata(amendment.status)
        out.endDateSemantics    = refdata(amendment.endDateSemantics)

        // id only — deliberately narrower than
        // views/licenseAmendment/_licenseAmendment.gson, which also renders the
        // owner name. Carrying any mutable license field here would make every
        // amendment snapshot change when the parent is renamed, and the
        // changed-only guard would then emit an UPDATE per amendment on a plain
        // license rename. The License topic already carries that change.
        out.owner               = owner(amendment)

        out.contacts            = sortById((amendment.contacts ?: []).collect { contact((InternalContact) it) })
        out.tags                = sortById((amendment.tags ?: []).collect { tag((Tag) it) })
        out.docs                = sortById((amendment.docs ?: []).collect { doc((DocumentAttachment) it) })
        out.supplementaryDocs   = sortById((amendment.supplementaryDocs ?: []).collect { doc((DocumentAttachment) it) })
        out.links               = sortById((amendment.links ?: []).collect { link((LicenseLink) it) })


        return out
    }

    /**
     * Lean pre-delete projection — scalars and the owner link only.
     *
     * DELETE signals removal and identifies what was removed; the state that
     * existed beforehand is already on the topic from the preceding CREATE and
     * UPDATE events. Skipping the hasMany collections also means this never
     * forces a lazy load, which matters because it runs from a PreDeleteEvent
     * listener mid-flush, and on the cascade path the parent License is being
     * torn down around it.
     */
    static Map<String, Object> deleteProjection(LicenseAmendment amendment) {
        if (amendment == null) return null

        Map<String, Object> out = [:]
        out.id                  = amendment.id
        out.name                = amendment.name
        out.description         = amendment.description
        out.dateCreated         = asString(amendment.dateCreated)
        out.lastUpdated         = asString(amendment.lastUpdated)
        out.startDate           = asString(amendment.startDate)
        out.endDate             = asString(amendment.endDate)
        out.status              = refdata(amendment.status)
        out.endDateSemantics    = refdata(amendment.endDateSemantics)
        out.owner               = owner(amendment)

        return out
    }

    private static Map owner(LicenseAmendment amendment) {
        if (amendment.owner == null) return null
        // .id off a lazy proxy does not force initialisation — which matters on
        // the CREATE path, where this runs mid-flush.
        [id: amendment.owner.id]
    }

    /**
     * Impose a deterministic order on a collection projection.
     *
     * The source {@code hasMany} collections are Hibernate Sets over domain
     * classes that define no {@code equals}/{@code hashCode}, so iteration
     * order follows identity hash codes and differs between two loads of the
     * same rows.
     */
    private static List sortById(List items) {
        items.sort { Object item -> (String) (((Map) item)?.id ?: '') }
    }

    // ---- shared mappers ----------------------------------------------------

    private static Map refdata(RefdataValue rv) {
        if (rv == null) return null
        [id: rv.id, value: rv.value, label: rv.label]
    }

    private static Map contact(InternalContact c) {
        if (c == null) return null
        [id: c.id, user: c.user, role: refdata(c.role)]
    }

    private static Map tag(Tag t) {
        if (t == null) return null
        [id: t.id, value: t.value]
    }

    private static Map doc(DocumentAttachment d) {
        if (d == null) return null
        // Deliberately excludes fileUpload — the GET template expands it into
        // web-toolkit's file-upload view, and an event builder must not risk
        // an S3/minio call from the event-publishing path.
        [
            id          : d.id,
            name        : d.name,
            location    : d.location,
            url         : d.url,
            note        : d.note,
            atType      : refdata(d.atType),
            dateCreated : asString(d.dateCreated),
            lastUpdated : asString(d.lastUpdated)
        ]
    }

    private static Map link(LicenseLink l) {
        if (l == null) return null
        [
            id        : l.id,
            linkType  : l.linkType,
            linkId    : l.linkId,
            linkLabel : l.linkLabel,
            relation  : l.relation,
            direction : l.direction
        ]
    }

    // ISO-8601, seconds precision, UTC — matches the REST GET representation.
    private static String asString(Object o) {
        if (o == null) return null
        if (o instanceof Date) {
            return ((Date) o).toInstant().truncatedTo(ChronoUnit.SECONDS).toString()
        }
        return o.toString()
    }
}