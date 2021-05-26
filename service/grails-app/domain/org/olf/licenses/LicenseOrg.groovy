package org.olf.licenses
import org.olf.general.Org
import com.k_int.web.toolkit.domain.traits.Clonable
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import grails.gorm.MultiTenant


/**
 * Link a subscription agreement with an org and attach a role
 */
public class LicenseOrg implements MultiTenant<LicenseOrg>, Clonable<LicenseOrg>{

  String id
  Org org
  boolean primaryOrg = false

  String note

  static hasMany = [
    roles: LicenseOrgRole,
  ]

  static mappedBy = [
    roles: 'owner',
  ]

  static belongsTo = [
    owner: License
  ]

    static mapping = {
                   id column: 'sao_id', generator: 'uuid2', length:36
              version column: 'sao_version'
                owner column: 'sao_owner_fk'
                  org column: 'sao_org_fk'
                 note column: 'sao_note', type: 'text'
           primaryOrg column: 'sao_primary_org'
               roles cascade: 'all-delete-orphan', lazy: false
  }

  static constraints = {
    owner(nullable:false, blank:false)
    org(nullable:true)
    note(nullable:true, blank:false)
    primaryOrg(nullable:false, blank:false)
  }
  
  @Override
  public LicenseOrg clone () {
    Clonable.super.clone()
  }
}
