package org.olf.licenses

import java.time.LocalDate

import org.olf.general.DocumentAttachment

import com.k_int.web.toolkit.custprops.CustomProperties
import com.k_int.web.toolkit.custprops.CustomProperty
import com.k_int.web.toolkit.custprops.types.CustomPropertyContainer
import com.k_int.web.toolkit.domain.traits.Clonable
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.tags.Tag

import grails.gorm.MultiTenant
import grails.gorm.annotation.Entity

@Entity
abstract class LicenseCore implements CustomProperties,MultiTenant<LicenseCore> {

  String id
  String name
  String description
  Date dateCreated
  Date lastUpdated
  LocalDate startDate
  LocalDate endDate

  /**
   * We must specify the category ID here. If we didn't the category would be named
   * LicenseCore.Status instead of License.Status
   */
  @CategoryId(value='License.Status', defaultInternal=true)
  @Defaults(['In negotiation','Not yet active', 'Active', 'Rejected', 'Expired'])
  RefdataValue status

  /**
   * This is an internal category. Set the default internal here explicitly. When we provide multiple
   * values to an annotation we have to explicitly declare the `value` field
   */
  @CategoryId(value='License.EndDateSemantics', defaultInternal=true)
  @Defaults(['Explicit', 'Open ended', 'Implicit'])
  RefdataValue endDateSemantics

  static hasMany = [
    links:LicenseLink,
    tags:Tag,
    docs: DocumentAttachment,
    contacts: InternalContact,
    supplementaryDocs: DocumentAttachment
  ]

  static mappedBy = [
    links:'owner',
    contacts: 'owner'
  ]

  static constraints = {
                name(nullable:false, blank:false)
         description(nullable:true, blank:false)
              status(nullable:true, blank:false)
         dateCreated(nullable:true, blank: true)
         lastUpdated(nullable:true, blank: true)
           startDate(nullable:true, blank: true)
             endDate(nullable:true, blank: true)
    endDateSemantics(nullable:true, blank: true)
  }

  static mapping = {
                table name: 'license'
                   id column: 'lic_id', generator: 'uuid2', length:36
                 name column: 'lic_name'
          description column: 'lic_description', type:'text'
               status column: 'lic_status_rdv_fk'
              version column: 'lic_version'
          dateCreated column: 'lic_date_created'
          lastUpdated column: 'lic_last_updated'
            startDate column: 'lic_start_date'
              endDate column: 'lic_end_date'
     endDateSemantics column: 'lic_end_date_semantics_fk'
                tags cascade: 'save-update', joinTable: [name: 'license_tag', key: 'license_tags_id']
                links cascade: 'all-delete-orphan'
             contacts cascade: 'all-delete-orphan'
                 docs cascade: 'all-delete-orphan', joinTable: [name: 'license_document_attachment', key: 'license_docs_id']
    supplementaryDocs cascade: 'all-delete-orphan', joinTable: [name: 'license_supp_doc', key: 'licsd_lic_fk', column: 'licsd_da_fk']
     customProperties cascade: 'all-delete-orphan'
  }

  static transients = ['openEnded']

  public boolean isOpenEnded() {
    boolean result = false;
    if ( endDateSemantics?.value?.equalsIgnoreCase('open_ended') )
      result = true;

    return result;
  }

  public void setOpenEnded(final boolean value) {
    // Determine/Set everything we can here.
    final String cat_desc = LicenseCore.getEndDateSemanticsCategory()
    // This will only overwrite 'implicit' with 'explicit' where 'openEnded' is also sent. For FOLIO this works because we always send everything.
    final String norm_value = value == true ? RefdataValue.normValue('Open ended') : (endDate != null ? RefdataValue.normValue('Explicit') : RefdataValue.normValue('Implicit'))
    // Just directly query.
    RefdataValue rdv = RefdataValue.createCriteria().get {
      createAlias ('owner', 'cat')
      and {
        eq 'cat.desc', cat_desc
        eq 'value', norm_value
      }
    }
    if (rdv) this.setEndDateSemantics( rdv )
  }

  /**
   * @param la LicenseCore
   * @return this license but with the custom properties (Terms) aggregated.
   */
  public LicenseCore plus (LicenseCore la) {

    final Set<String> seen = []
    List<CustomProperties> newList = la.customProperties.value.findResults { CustomProperty v ->
      if (seen.contains(v.definition.name)) {
        return null
      }

      seen << v.definition.name
      v
    }

    newList += this.customProperties.value.findResults { CustomProperty v ->
      if (seen.contains(v.definition.name)) {
        return null
      }

      seen << v.definition.name
      v
    }

    this.customProperties = new CustomPropertyContainer()
    this.customProperties.value = newList as Set
    this
  }
}
