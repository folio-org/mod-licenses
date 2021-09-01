package org.olf.licenses

import grails.gorm.transactions.Transactional
import org.olf.general.Org
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
    triggerOrgsCleanup();
  }

  private List<LicenseAmendment> batchFetchAmendmentsWithEnddatesemanticsIsNull(final int amendmentBatchSize, int amendmentBatchCount) {
    List<LicenseAmendment> amendments = LicenseAmendment.createCriteria().list ([max: amendmentBatchSize, offset: amendmentBatchSize * amendmentBatchCount]) {
      isNull("endDateSemantics")
      order 'id'
    }
    return amendments
  }

  private List<License> batchFetchLicensesWithEnddatesemanticsIsNull(final int licenseBatchSize, int licenseBatchCount) {
    List<License> licenses = License.createCriteria().list ([max: licenseBatchSize, offset: licenseBatchSize * licenseBatchCount]) {
      isNull("endDateSemantics")
      order 'id'
    }
    return licenses
  }

  def checkUnsetValues() {
    log.debug("EndDateSemanticsCleanup: Check for unset values")

    def count = 0
    def batchSize = 25

    License.withNewTransaction {
      List<License> licenses = batchFetchLicensesWithEnddatesemanticsIsNull(batchSize, count)
      while (licenses && licenses.size() > 0) {
        count++
        licenses.each {License.findAllByEndDateSemanticsIsNull().each { lic ->
            lic.endDateSemantics = RefdataValue.lookupOrCreate('endDateSemantics', 'Implicit')
            lic.save(flush:true, failOnError:true)
          }
        }
        // Next page
        licenses = batchFetchLicensesWithEnddatesemanticsIsNull(batchSize, count)
      }
    }

    count = 0

    LicenseAmendment.withNewTransaction {
      List<LicenseAmendment> amendments = batchFetchAmendmentsWithEnddatesemanticsIsNull(batchSize, count)
      while (amendments && amendments.size() > 0) {
        count++
        amendments.each {LicenseAmendment.findAllByEndDateSemanticsIsNull().each { la ->
            la.endDateSemantics = RefdataValue.lookupOrCreate('endDateSemantics', 'Implicit')
            la.save(flush:true, failOnError:true)
          }
        }
        // Next page
        amendments = batchFetchAmendmentsWithEnddatesemanticsIsNull(batchSize, count)
      }
    }
  }

  void triggerOrgsCleanup() {
    log.debug("LicenseHousekeepingService::triggerOrgsCleanup")
    def orgCountBeforeCleanup = Org.executeQuery("""SELECT COUNT(*) FROM Org""".toString())[0]

    Org.executeUpdate("""
      DELETE from Org as theOrg WHERE NOT EXISTS (
        FROM LicenseOrg as lo
          WHERE lo.org = theOrg
      )""".toString()
    )

    def orgCountAfterCleanup = Org.executeQuery("""SELECT COUNT(*) FROM Org""".toString())[0]
    log.debug("triggerOrgsCleanup removed ${orgCountBeforeCleanup - orgCountAfterCleanup} Org records")
  }

}
