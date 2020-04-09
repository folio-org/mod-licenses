package org.olf.licenses


import grails.core.GrailsApplication
import grails.gorm.multitenancy.CurrentTenant
import grails.plugins.*
import groovy.util.logging.Slf4j

@Slf4j
class ApplicationController implements PluginManagerAware {

  GrailsApplication grailsApplication
  GrailsPluginManager pluginManager
  DataloadService dataloadService

  def index() {
    [grailsApplication: grailsApplication, pluginManager: pluginManager]
  }
  
  @CurrentTenant
  def config() {
    if ( request.method=='POST' ) {
      log.debug("ApplicationController::config POST");
      if ( request.JSON ) {
        dataloadService.upsertConfigJson(request.JSON);
      }
    }
  }
}
