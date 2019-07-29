package org.olf.licenses

import org.apache.commons.lang.math.RandomUtils

import grails.testing.mixin.integration.Integration
import groovy.util.logging.Slf4j
import spock.lang.Shared
import spock.lang.Stepwise
import spock.lang.Unroll

/**
 * Test the new amendment abstraction model.
 */
@Slf4j
@Integration
@Stepwise
class LicenseAmendmentSpec extends LicenseLifecycleSpec {
  
  @Shared ammendments = []
  
  @Unroll
  void 'Add amendments to license #licenseId' (licenseId) {
    
    given: 'Read licence'
      Map httpResult = doGet("/licenses/licenses/${licenseId}")

    and: 'Check current amendments'
      final int currentAmends = httpResult.amendments?.size() ?: 0
      
    and: 'Add amendment'
      httpResult = doPut("/licenses/licenses/${licenseId}") {
        amendments ([{
          name "Amendment 1"
          description "My first added amendment"
          status "Active"
          startDate "2019-03-01"
          openEnded true
          customProperties {
            concurrentAccess ([15])
            remoteAccess (["No"])
          }
        }
      ])
    }
      
    expect: 'Amendments increase by 1'
      httpResult.amendments.size() == (currentAmends + 1)

    where:
      licenseId << data['licenses'].collect { name, val -> val.id }
  }
  
  void 'Test amendments serialized out' () {
    given: 'Read first license'
      Map httpResult = doGet("/licenses/licenses/${data['licenses'].values().getAt(0).get('id')}")
      
    expect: 'Amendments should contain more than just the id property'
      assert httpResult.amendments.every {
        it.keySet().size() > 1
      }
      
    and: 'Amendment status should be active'
      // Check first amendment has status property
      assert httpResult['amendments'][0]['status']['value'] == 'active'
  }
  
  @Unroll
  void 'Remove amendment from #licenseId' (licenseId) {
      
    given: 'Read licence'
      Map httpResult = doGet("/licenses/licenses/${licenseId}")
      
    and: 'Check current amendments'
      final int currentAmends = httpResult.amendments?.size() ?: 0
      
    and: 'Record random amendment'
      int randomIndex = RandomUtils.nextInt(httpResult.amendments.size())
      Map amendment = httpResult.amendments[randomIndex]
      
    and: 'Remove amendment and put license back'
      amendment['_delete'] = true
      httpResult = doPut("/licenses/licenses/${licenseId}", httpResult)
      
    expect: 'Amendment has gone'
      assert httpResult.amendments.size() == (currentAmends - 1)
      assert httpResult.amendments.find { it.id == amendment.id } == null

    where:
      licenseId << data['licenses'].collect { name, val -> val.id }
  }
}

