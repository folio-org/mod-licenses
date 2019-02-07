package org.olf.licenses

import com.k_int.okapi.OkapiTenantAdminService

class BootStrap {
  
    OkapiTenantAdminService okapiTenantAdminService
    def init = { servletContext ->
      okapiTenantAdminService.freshenAllTenantSchemas()
    }
    def destroy = {
    }
}
