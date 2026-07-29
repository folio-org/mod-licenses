package org.olf.licenses.events

import java.time.temporal.ChronoUnit

import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.tags.Tag
import groovy.transform.CompileStatic
import org.olf.general.DocumentAttachment
import org.olf.general.Org
import org.olf.licenses.AlternateName
import org.olf.licenses.InternalContact
import org.olf.licenses.License
import org.olf.licenses.LicenseAmendment
import org.olf.licenses.LicenseLink
import org.olf.licenses.LicenseOrg
import org.olf.licenses.LicenseOrgRole

/**
 * Builds the JSON-ready Map payload for a {@link License} domain event.
 *
 * Local {@code hasMany} collections (alternateNames, orgs, contacts, tags,
 * docs, supplementaryDocs, links) are serialized as full nested objects.
 * {@code amendments} — a first-class child with its own event stream — is
 * ID-refs only, so consumers detect "line added or removed" by diffing
 * {@code old.amendments} against {@code new.amendments}.
 *
 * Must be invoked while the Hibernate session is active so lazy collections
 * resolve rather than throwing later.
 */
@CompileStatic
class LicenseSnapshotBuilder {

    static Map<String, Object> snapshot(License license) {
        if (license == null) return null

        Map<String, Object> out = [:]
        out.id                = license.id
        out.name              = license.name
        out.description       = license.description
        out.localReference     = license.localReference
        out.dateCreated        = asString(license.dateCreated)
        out.lastUpdated        = asString(license.lastUpdated)
        out.startDate          = asString(license.startDate)
        out.endDate            = asString(license.endDate)
        out.openEnded          = license.openEnded

        out.status             = refdata(license.status)
        out.endDateSemantics    = refdata(license.endDateSemantics)
        out.type                = refdata(license.type)

        out.alternateNames     = (license.alternateNames ?: []).collect { altName((AlternateName) it) }
        out.orgs               = (license.orgs ?: []).collect { licenseOrg((LicenseOrg) it) }
        out.contacts           = (license.contacts ?: []).collect { contact((InternalContact) it) }
        out.tags               = (license.tags ?: []).collect { tag((Tag) it) }
        out.docs               = (license.docs ?: []).collect { doc((DocumentAttachment) it) }
        out.supplementaryDocs   = (license.supplementaryDocs ?: []).collect { doc((DocumentAttachment) it) }
        out.links               = (license.links ?: []).collect { link((LicenseLink) it) }

        out.amendments          = (license.amendments ?: []).collect { [id: ((LicenseAmendment) it).id] }

        return out
    }

    private static Map refdata(RefdataValue rv) {
        if (rv == null) return null
        [id: rv.id, value: rv.value, label: rv.label]
    }

    private static Map orgWrapper(Org o) {
        if (o == null) return null
        [id: o.id, name: o.name, orgsUuid: o.orgsUuid]
    }

    private static Map licenseOrg(LicenseOrg lo) {
        if (lo == null) return null
        [
            id         : lo.id,
            primaryOrg : lo.primaryOrg,
            note       : lo.note,
            roles      : (lo.roles ?: []).collect { licenseOrgRole((LicenseOrgRole) it) },
            org        : orgWrapper(lo.org)
        ]
    }

    private static Map licenseOrgRole(LicenseOrgRole r) {
        if (r == null) return null
        [id: r.id, role: refdata(r.role)]
    }

    private static Map contact(InternalContact c) {
        if (c == null) return null
        [id: c.id, user: c.user, role: refdata(c.role)]
    }

    private static Map altName(AlternateName a) {
        if (a == null) return null
        [id: a.id, name: a.name]
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

