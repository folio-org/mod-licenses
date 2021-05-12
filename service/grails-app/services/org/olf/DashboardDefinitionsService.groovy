package org.olf

import org.grails.io.support.PathMatchingResourcePatternResolver
import org.grails.io.support.Resource
import groovy.json.JsonSlurper

class DashboardDefinitionsService {
  PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver()
  def jsonSlurper = new JsonSlurper()

  public getWidgetDefinitions() {
    Resource[] widgetDefs = resolver.getResources("classpath:sample_data/widgetDefinitions/*")
    ArrayList definitions = [];
    widgetDefs.each { resource ->
      def stream = resource.getInputStream()
      def wd = jsonSlurper.parse(stream)
      definitions << wd
    }
    definitions
  }

}