package org.olf.licenses

import grails.gorm.multitenancy.CurrentTenant
import grails.converters.JSON
import grails.gorm.transactions.Transactional

@CurrentTenant
class AdminController {

  def licenseHousekeepingService
  def fileUploadService

  public AdminController() {
  }


  public triggerHousekeeping() {
    def result = [:]
    licenseHousekeepingService.triggerHousekeeping()
    result.status = 'OK'
    render result as JSON
  }

  /**
   * Trigger migration of uploaded LOB objects from PostgresDB to configured S3/MinIO
   */
  @Transactional
  public triggerDocMigration() {
    def result = [:]
    log.debug("AdminController::triggerDocMigration");
    fileUploadService.migrateAtMost(0,'LOB','S3'); // n, FROM, TO
    result.status = 'OK'
    render result as JSON
  }

}
