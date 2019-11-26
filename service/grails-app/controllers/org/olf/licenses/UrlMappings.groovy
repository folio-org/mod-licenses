package org.olf.licenses

class UrlMappings {

  static mappings = {

    "/"(controller: 'application', action:'index')
    "/licenses/licenses"(resources:'license') {
      "/linkedAgreements" {
        namespace         = 'okapi'
        controller        = 'resourceProxy'
        targetPath        = '/erm/sas/linkedLicenses'
        defaultParams     = [
          'filters':[
            { "remoteId==${params.licenseId}" }
          ]
        ]
        withParameters    = true
      }
    }
    
    
    "/licenses/licenseLinks"(resources:'licenseLink')

    '/licenses/contacts'(resources: 'internalContact')

    '/licenses/refdata'(resources: 'refdata') {
      collection {
        "/$domain/$property" (controller: 'refdata', action: 'lookup')
        
      }
    }

    '/licenses/custprops'(resources: 'customPropertyDefinition') {
      collection {
        "/" (controller: 'customPropertyDefinition', action: 'index') {
          perPage = { params.perPage ?: 100 }
//          sort = [ 'primary;desc', 'weight;asc', 'id;asc']
        }
      }
    }

    '/licenses/org'(resources: 'org') {
      collection {
        "/find/$id"(controller:'org', action:'find')
      }
    }
    
    "/licenses/files" ( resources:'fileUpload', excludes: ['update', 'patch', 'save', 'edit', 'create']) {
      collection {
        '/' (controller: "fileUpload", action: "uploadFile", method: 'POST')
      }
      "/raw" ( controller: "fileUpload", action: "downloadFile", method: 'GET' )
    }

    "500"(view: '/error')
    "404"(view: '/notFound')
  }
}
