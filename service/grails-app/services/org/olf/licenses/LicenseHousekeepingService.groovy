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

          int i = 0
          License.findAllByEndDateSemanticsIsNull().each {lic ->
            lic.endDateSemantics = RefdataValue.lookupOrCreate('endDateSemantics','Implicit')
            lic.save()
            i++
          }
          if (i>0) log.debug("Updated endDateSemantics for ${i} License(s), setting it to 'implicit'")

          int j = 0
          LicenseAmendment.findAllByEndDateSemanticsIsNull().each {la ->
            la.endDateSemantics = RefdataValue.lookupOrCreate('endDateSemantics','Implicit')
            la.save()
            j++
          }
          if (j>0) log.debug("Updated endDateSemantics for ${j} License Amendment(s), setting it to 'implicit'")
    }
}
