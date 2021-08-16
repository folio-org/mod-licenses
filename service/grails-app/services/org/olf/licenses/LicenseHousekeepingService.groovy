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

  private List<List<String>> batchFetchAmendments(final int platformBatchSize, int platformBatchCount) {
    List<List<String>> amendments = LicenseAmendment.createCriteria().list ([max: platformBatchSize, offset: platformBatchSize * platformBatchCount]) {
      order 'id'
    }
    return amendments
  }

  private List<List<String>> batchFetchLicenses(final int platformBatchSize, int platformBatchCount) {
    List<List<String>> licenses = License.createCriteria().list ([max: platformBatchSize, offset: platformBatchSize * platformBatchCount]) {
      order 'id'
    }
    return licenses
  }

  def checkUnsetValues() {
    log.debug("EndDateSemanticsCleanup: Check for unset values")

    def count = 0
    def batchSize = 2

    License.withNewTransaction {
      List<License> licenses = batchFetchLicenses(batchSize, count)
      while (licenses && licenses.size() > 0) {
        count++
        licenses.each {License.findAllByEndDateSemanticsIsNull().each { la ->
            la.endDateSemantics = RefdataValue.lookupOrCreate('endDateSemantics', 'Implicit')
            la.save(flush:true, failOnError:true)
          }
        }
        // Next page
        licenses = batchFetchLicenses(batchSize, count)
      }
    }

    count = 0

    LicenseAmendment.withNewTransaction {
      List<LicenseAmendment> amendments = batchFetchAmendments(batchSize, count)
      while (amendments && amendments.size() > 0) {
        count++
        amendments.each {LicenseAmendment.findAllByEndDateSemanticsIsNull().each { la ->
            la.endDateSemantics = RefdataValue.lookupOrCreate('endDateSemantics', 'Implicit')
            la.save(flush:true, failOnError:true)
          }
        }
        // Next page
        amendments = batchFetchAmendments(batchSize, count)
      }
    }
  }
}
