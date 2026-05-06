package org.olf.licenses

import com.k_int.accesscontrol.grails.AccessControlUrlMapping

class UrlMappings {

  static mappings = {
    Closure buildAccessControlRoutes = AccessControlUrlMapping.buildRoutes(
      "/licenses",
      [
        [path: '/licenses', controller: 'license'],
        [path: '/amendments', controller: 'licenseAmendment']
      ]
    )

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

    "/dashboard/definitions" (controller: 'dashboardDefinitions', action: 'getDefinitions' ,method: 'GET')

    "404"(view: '/notFound')

    // Register access control routes last so that any failure here does not
    // abort registration of the routes above (Grails evaluates the mappings
    // closure top-to-bottom; an exception before all routes are declared leaves
    // those routes un-registered).
    buildAccessControlRoutes.delegate = delegate
    try {
      buildAccessControlRoutes.call()
    } catch (e) {
      log.error("Failed to register access control routes: ${e.message}", e)
    }
  }
}
