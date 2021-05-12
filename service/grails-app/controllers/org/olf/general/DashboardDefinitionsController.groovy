package org.olf.general

import grails.gorm.multitenancy.CurrentTenant
import groovy.util.logging.Slf4j

@Slf4j
@CurrentTenant
class DashboardDefinitionsController  {
  def dashboardDefinitionsService
  public getDefinitions() {
    respond dashboardDefinitionsService.getWidgetDefinitions();
  }
}

