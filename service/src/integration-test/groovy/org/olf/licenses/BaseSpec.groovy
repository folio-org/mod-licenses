package org.olf.licenses

import com.k_int.okapi.OkapiHeaders
import com.k_int.web.toolkit.testing.HttpSpec

import groovyx.net.http.HttpException
import spock.lang.Stepwise
import spock.util.concurrent.PollingConditions

@Stepwise
abstract class BaseSpec extends HttpSpec {
  def setupSpec() {
    httpClientConfig = {
      client.clientCustomizer { HttpURLConnection conn ->
        conn.connectTimeout = 3000
        conn.readTimeout = 20000
      }
    }
    addDefaultHeaders(
      (OkapiHeaders.TENANT): "${this.class.simpleName}",
      (OkapiHeaders.USER_ID): "${this.class.simpleName}_user"
    ) 
  }
  
  Map<String, String> getAllHeaders() {
    state.specDefaultHeaders + headersOverride
  }
  
  String getCurrentTenant() {
    allHeaders?.get(OkapiHeaders.TENANT)
  }
  
  void 'Pre purge tenant' () {
    boolean resp = false
    when: 'Purge the tenant'
      try {
        resp = doDelete('/_/tenant', null)
        resp = true
      } catch (HttpException ex) { resp = true }
      
    then: 'Response obtained'
      resp == true
  }
  
  void 'Ensure test tenant' () {
    
    when: 'Create the tenant'
      def resp = doPost('/_/tenant', {
      parameters ([["key": "loadReference", "value": true]])
    })

    then: 'Response obtained'
    resp != null

    and: 'License terms added'

    List list
    // Wait for the customProperties to be loaded.
    
    // Max time to wait is 10 seconds
    def conditions = new PollingConditions(timeout: 10)
    conditions.eventually {
      (list = doGet('/licenses/custprops')).size() > 0
    }
  }
}
