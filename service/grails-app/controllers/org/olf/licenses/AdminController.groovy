package org.olf.licenses

import grails.gorm.multitenancy.CurrentTenant
import grails.web.databinding.DataBinder
//import groovy.util.logging.Slf4j
import grails.converters.JSON

//@Slf4j
@CurrentTenant
class AdminController implements DataBinder{

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

