package org.olf.licenses
import org.olf.general.Org
import com.k_int.web.toolkit.domain.traits.Clonable
import com.k_int.web.toolkit.refdata.CategoryId
import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import grails.compiler.GrailsCompileStatic
import grails.gorm.MultiTenant


/**
 * Link a license with an org and attach a role
 */
@GrailsCompileStatic
public class LicenseOrgRole implements MultiTenant<LicenseOrgRole>, Clonable<LicenseOrgRole> {
  
  String id

  @CategoryId(value='LicenseOrg.Role', defaultInternal=false) // make an entry in refdata_category with the same description as it was before the restructuring of license org roles
  @Defaults(['Licensor']) // we need at least one default value as the category isn't created otherwise
  RefdataValue role
  String note
  
  static belongsTo = [
    owner: LicenseOrg
  ]

    static mapping = {
                   id column: 'lior_id', generator: 'uuid2', length:36
              version column: 'lior_version'
                owner column: 'lior_owner_fk'
                 role column: 'lior_role_fk'
                 note column: 'lior_note', type: 'text'
  }

  static constraints = {
    owner(nullable:false, blank:false);
    role(nullable:false, blank:false);
    note(nullable:true, blank:false);
  }
  
  /**
   * Need to resolve the conflict manually and add the call to the clonable method here. 
   */
  @Override
  public LicenseOrgRole clone () {
    Clonable.super.clone()
  }
}
