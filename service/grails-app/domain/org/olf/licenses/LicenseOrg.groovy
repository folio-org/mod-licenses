package org.olf.licenses
import org.olf.general.Org

import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataValue

import grails.gorm.MultiTenant


/**
 * Link a subscription agreement with an org and attach a role
 */
public class LicenseOrg implements MultiTenant<LicenseOrg>{

  String id
  Org org

  @Defaults(['Licensor', 'Licensee', 'Consortium', 'Consortium Administrator'])
  RefdataValue role

  static belongsTo = [
    owner: License
  ]

    static mapping = {
                   id column: 'sao_id', generator: 'uuid2', length:36
              version column: 'sao_version'
                owner column: 'sao_owner_fk'
                  org column: 'sao_org_fk'
                 role column: 'sao_role'
  }

  static constraints = {
    owner(nullable:false, blank:false);
    org(nullable:true, blank:false);
    role(nullable:true, blank:false);
  }
}
