package org.olf.licenses

import com.k_int.web.toolkit.custprops.CustomProperties
import com.k_int.web.toolkit.custprops.types.CustomPropertyContainer
import com.k_int.web.toolkit.domain.traits.Clonable
import com.k_int.web.toolkit.tags.Tag
import grails.gorm.MultiTenant
import org.olf.general.Org
import org.olf.general.DocumentAttachment
import com.k_int.web.toolkit.refdata.RefdataValue;
import com.k_int.web.toolkit.refdata.Defaults;

class LicenseAmendment extends LicenseCore implements CustomProperties,MultiTenant<LicenseAmendment>, Clonable<LicenseAmendment>  {  
  License owner

  static copyByCloning = ['customProperties', 'docs', 'supplementaryDocs', 'contacts']
  static cloneStaticValues = [
    name: { "Copy of: ${owner.name}" /* Owner is the current object. */ },
    version: { 0 }
  ] 

  static belongsTo = [
    owner: License
  ]
  
  static constraints = {
    owner (nullable:false, blank:false)
  }

  static mapping = {
    owner column: 'am_owning_license_fk'
  }

  @Override
  public LicenseAmendment clone () {
    Clonable.super.clone()
  }
}
