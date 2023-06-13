package org.olf.licenses

import static org.springframework.http.HttpStatus.*

import com.k_int.okapi.OkapiTenantAwareController
import com.k_int.web.toolkit.refdata.RefdataValue

import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.transactions.Transactional
import org.grails.web.json.JSONObject


/**
 * Control access to subscription agreements.
 * A subscription agreement (SA) is the connection between a set of resources (Which could be packages or individual titles) and a license. 
 * SAs have start dates, end dates and renewal dates. This controller exposes functions for interacting with the list of SAs
 */
@CurrentTenant
class LicenseController extends OkapiTenantAwareController<License> {

  LicenseService licenseService
  
  LicenseController() {
    super(License)
  }
  
  // Override the show method
  @Transactional(readOnly=true)
  def show() {
    // Applicable amendments might be present.
    final List<Serializable> am = params.list('applyAmendment')
    if (!am) {
      // Just follow the super implementation
      return super.show()
    }
    
    // Otherwise let's mutate the original license and only supply the applicable terms.
    License license = params.id ? License.read(params.id) : null
    
    if (license) {
      // Lookup the active status first.
      RefdataValue active = LicenseAmendment.lookupStatus('active')
      
      // We found a license. Lets append the active amendments by startdate
      List<LicenseAmendment> applicableAmendments = LicenseAmendment.createCriteria().list {
        'in' 'id', am
        eq 'owner', license
        eq 'status', active
        order 'startDate', 'asc'
      }
      
      if (applicableAmendments) {
        license += applicableAmendments.sum()
      }
    }
    
    respond license
  }
  
  
  private static final Map<String, List<String>> CLONE_GROUPING = [    
    licenseInfo: ['name', 'type', 'description', 'status'],
    internalContacts: ['contacts'],
    organizations: ['orgs'],
    coreDocs: ['docs'],
    terms: ['customProperties'],
    licenseDateInfo: ['endDateSemantics', 'startDate', 'endDate']
//    supplementaryDocs: ['supplementaryDocs']
  ]
  
  @Transactional
  def doClone () {
    final Set<String> props = []
    final String licenseId = params.get("licenseId")
    if (licenseId) {
      
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
      
      log.debug "Attempting to clone license ${licenseId} using props ${props}"
      License instance = queryForResource(licenseId).clone(props)
      
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
  
  @Transactional
  def delete() {
    License license = queryForResource(params.id)
    
    // Not found.
    if (license == null) {
      transactionStatus.setRollbackOnly()
      notFound()
      return
    }
    
    // Return the relevant status if not allowed to delete.
    List<Serializable> agreementIds = licenseService.checkAttachedAgreements(license)
    if (agreementIds.size() > 0) {
      transactionStatus.setRollbackOnly()
      render status: METHOD_NOT_ALLOWED, text: "License is attached to agreements ${agreementIds.join(', ')}"
      return
    }
    
    // Finally delete the license if we get this far and respond.
    deleteResource license
    render status: NO_CONTENT
  }
}

