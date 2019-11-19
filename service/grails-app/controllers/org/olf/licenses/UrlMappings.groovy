package org.olf.licenses

import org.olf.general.FileUpload

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

    
    get "/licenses/files/$id/raw"(controller: "fileUpload", action: "getFileUploadRaw")
    get "/licenses/files/$id"(controller: "fileUpload", action: "getFileUpload")
    get '/licenses/files'(controller: "fileUpload", action: "getFileUploadList")
    post '/licenses/files'(controller: "fileUpload", action: "postFileUploadRaw")
    delete "/licenses/files/$id"(controller: "fileUpload", action: "deleteFileUpload")

    "500"(view: '/error')
    "404"(view: '/notFound')
  }
}
