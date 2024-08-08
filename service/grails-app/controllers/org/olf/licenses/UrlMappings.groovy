package org.olf.licenses

class UrlMappings {

  static mappings = {

    "/"(controller: 'application', action:'index')
    "/licenses/licenses"(resources:'license') {

      collection {
        "/compareTerms" (controller: 'export', method: 'POST', format: 'csv')
      }

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

      '/clone' (controller: 'license', action: 'doClone', method: 'POST')
    }

    "/licenses/amendments" (resources:'licenseAmendment') {
      '/clone' (controller: 'licenseAmendment', action: 'doClone', method: 'POST')
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
        "/" (controller: 'customPropertyDefinition', action: 'index')
        "/contexts" (controller: 'customPropertyDefinition', action: "fetchContexts", method: 'GET')
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

    "/licenses/admin/$action"(controller:'admin')

    "/licenses/settings/appSettings" (resources: 'setting');

    "/licenses/swagger/api"(controller: 'swaggerUI', action:'api')

    // "/licenses/swaggerUI"(uri: '/static/swaggerUI/index.html')


    // group "/licenses/swagger", {
    //   "/swagger-ui.css"(uri: '/static/swaggerUI/5.17.14/swagger-ui.css')
    //   "/swagger-ui-standalone-preset.js"(uri: '/static/swaggerUI/5.17.14/swagger-ui-standalone-preset.js')
    //   "/swagger-initializer.js"(uri: '/static/swaggerUI/5.17.14/swagger-initializer.js')
    //   "/index.css"(uri: '/static/swaggerUI/5.17.14/index.css')
    //   "/swagger-ui-bundle.js"(uri: '/static/swaggerUI/5.17.14/swagger-ui-bundle.js')
    //   "/favicon-32x32.png"(uri: '/static/swaggerUI/5.17.14/favicon-32x32.png')
    //   "/favicon-16x16.png"(uri: '/static/swaggerUI/5.17.14/favicon-16x16.png')
    // }


    "/dashboard/definitions" (controller: 'dashboardDefinitions', action: 'getDefinitions' ,method: 'GET')

    "500"(view: '/error')
    "404"(view: '/notFound')
  }
}
