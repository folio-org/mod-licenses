package org.olf.licenses

import grails.gorm.multitenancy.CurrentTenant
import grails.converters.JSON

@CurrentTenant
class AdminController {

  def licenseHousekeepingService

  public AdminController() {
  }


  public triggerHousekeeping() {
    def result = [:]
    licenseHousekeepingService.triggerHousekeeping()
    result.status = 'OK'
    render result as JSON
  }
}
