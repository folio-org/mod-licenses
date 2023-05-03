package org.olf.licenses

import static org.springframework.http.HttpStatus.*

import com.k_int.okapi.OkapiTenantAwareController
import com.k_int.web.toolkit.refdata.RefdataValue

import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.transactions.Transactional
import org.grails.web.json.JSONObject

@CurrentTenant
class LicenseAmendmentController extends OkapiTenantAwareController<LicenseAmendment> {
  LicenseAmendmentController() {
    super(LicenseAmendment)
  }

  private static final Map<String, List<String>> CLONE_GROUPING = [    
    amendmentInfo: ['name', 'type', 'description', 'status'],
    internalContacts: ['contacts'],
    organizations: ['orgs'],
    coreDocs: ['docs'],
    terms: ['customProperties'],
    amendmentDateInfo: ['endDateSemantics', 'startDate', 'endDate']
//    supplementaryDocs: ['supplementaryDocs']
  ]
  
  @Transactional
  def doClone () {
    final Set<String> props = []
    final String amendmentId = params.get("licenseAmendmentId")
    if (amendmentId) {
      
      // Grab the JSON body.
      JSONObject body = request.JSON
      
      // Build up a list of properties from the incoming json object.
      for (Map.Entry<String, Boolean> entry : body.entrySet()) {
        
        if (entry.value == true) {
        
          final String fieldOrGroup = entry.key
          if (CLONE_GROUPING.containsKey(fieldOrGroup)) {
            // Add the group instead.
            props.addAll( CLONE_GROUPING[fieldOrGroup] )
          } else {
            // Assume single field.
            props << fieldOrGroup
          }
        }
      }
      
      log.debug "Attempting to clone amendment ${amendmentId} using props ${props}"
      LicenseAmendment instance = queryForResource(amendmentId).clone(props)
      
      instance.save()
      if (instance.hasErrors()) {
        transactionStatus.setRollbackOnly()
        respond instance.errors, view:'edit' // STATUS CODE 422 automatically when errors rendered.
        return
      }
      respond instance, [status: OK]
      return
    }
    
    respond ([statusCode: 404])
  }
  

}