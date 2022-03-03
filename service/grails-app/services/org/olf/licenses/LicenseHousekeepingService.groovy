package org.olf.licenses

import com.k_int.okapi.OkapiTenantResolver
import grails.events.annotation.Subscriber
import grails.gorm.multitenancy.Tenants
import grails.gorm.transactions.Transactional
import org.olf.general.Org
import org.olf.licenses.License
import org.olf.licenses.LicenseAmendment
import com.k_int.web.toolkit.refdata.RefdataValue
import com.k_int.web.toolkit.settings.AppSetting
import groovy.util.logging.Slf4j

/**
 * This service works at the module level, it's often called without a tenant context.
 */
@Slf4j
@Transactional
public class LicenseHousekeepingService {

  def grailsApplication

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

  @Subscriber('okapi:dataload:reference')
  public void onLoadReference (final String tenantId, String value, final boolean existing_tenant, final boolean upgrading, final String toVersion, final String fromVersion) {
    log.info("LicenseHousekeepingService::onLoadReference(${tenantId},${value},${existing_tenant},${upgrading},${toVersion},${fromVersion})");
    final String tenant_schema_id = OkapiTenantResolver.getTenantSchemaName(tenantId)
    try {
      Tenants.withId(tenant_schema_id) {
        AppSetting.withTransaction {

          log.debug("Check app settings for file storage are in place");

          // Bootstrap refdata - controlled vocabulary of storage engines
          RefdataValue.lookupOrCreate('FileStorageEngines', 'LOB');
          RefdataValue.lookupOrCreate('FileStorageEngines', 'S3');

          def default_aws_region = grailsApplication.config.kiwt?.filestore?.aws_region
          def default_aws_url = grailsApplication.config.kiwt?.filestore?.aws_url
          def default_aws_secret = grailsApplication.config.kiwt?.filestore?.aws_secret
          def default_aws_bucket = grailsApplication.config.kiwt?.filestore?.aws_bucket
          def default_aws_access_key_id = grailsApplication.config.kiwt?.filestore?.aws_access_key_id

          // Bootstrap any app settings we may need
          [
            [ 'fileStorage', 'storageEngine', 'String', 'FileStorageEngines', 'LOB' ],
            [ 'fileStorage', 'S3Endpoint',    'String', null,                 default_aws_url ?: 'http://s3_endpoint_host.domain:9000' ],
            [ 'fileStorage', 'S3AccessKey',   'String', null,                 default_aws_access_key_id ?: 'ACCESS_KEY' ],
            [ 'fileStorage', 'S3SecretKey',   'String', null,                 default_aws_secret ?: 'SECRET_KEY' ],
            [ 'fileStorage', 'S3BucketName',  'String', null,                 default_aws_bucket ?: "${tenantId}-shared" ],
            [ 'fileStorage', 'S3BucketRegion','String', null,                 default_aws_region ?: "us-east-1" ],
            [ 'fileStorage', 'S3ObjectPrefix','String', null,                 "/${tenantId}/licenses/" ],
          ].each { st_row ->
            log.debug("Check app setting ${st_row}");

            AppSetting new_as = AppSetting.findBySectionAndKey(st_row[0], st_row[1]) ?: new AppSetting(
                                              section:st_row[0],
                                              key:st_row[1],
                                              settingType:st_row[2],
                                              vocab:st_row[3],
                                              value:st_row[4]).save(flush:true, failOnError:true);

          }
        }
      }
    }
    catch ( Exception e ) {
      log.error("Problem with load reference",e);
    }
  }

}
