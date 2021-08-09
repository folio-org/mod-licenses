package org.olf.licenses

import grails.gorm.transactions.Transactional
import org.olf.licenses.License
import org.olf.licenses.LicenseAmendment
import com.k_int.web.toolkit.refdata.RefdataValue

/**
 * This service works at the module level, it's often called without a tenant context.
 */
@Transactional
public class LicenseHousekeepingService {

  public void triggerHousekeeping() {
    this.checkUnsetValues();
  }


  def checkUnsetValues() {
        log.debug("EndDateSemanticsCleanup: Check for unset values")

          License lic = License.findByEndDateSemanticsIsNull()
          if (lic) {
            lic.endDateSemantics = RefdataValue.lookupOrCreate('endDateSemantics','Implicit')
            lic.save()
          }

          LicenseAmendment la = LicenseAmendment.findByEndDateSemanticsIsNull()
          if (la) {
            la.endDateSemantics = RefdataValue.lookupOrCreate('endDateSemantics','Implicit')
            la.save()
          }
    }
}
