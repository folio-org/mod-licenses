# mod-licenses

Copyright (C) 2018-2023 The Open Library Foundation

This software is distributed under the terms of the Apache License,
Version 2.0. See the file "[LICENSE](LICENSE)" for more information.

## Introduction

FOLIO service module - licenses.

The licenses module provides services to document licenses and their terms, and to link those licenses to arbitrary resources on the folio service platform. The actual license metadata 
is minimal, but mod-licenses then provides a user-extensible set of custom properties where librarians can define and document the terms of their license in a machine readable way. This readme
is split into two sections: The first gives information for developers wanting to use the services that mod-licenses exposes to the FOLIO LSP. The latter sections are intended for developers
looking to maintain and extend mod-licenses.

# For developers wanting to use mod-license resources

If you are a folio module developer looking to use mod-license services the following URL patterns may help you interact with the service:

## About linking

Some of the examples below talk about linking Licenses to other resources on the platform. This module provides a very open and extensible method for making these links, and it's
largely inspired by the semantic web subject-predicate-object model and the idea that anything can be linked to anything.

This freedom should be used carefully. Without careful curation of the model, this facility could easily descend into a rats nest of mismatched semantics, and it's really important
to think through your link structure and information architecture. It's particularly strongly suggested that you think about having hierarchical link types and reference IDs. Link types might
be made hierarchical by having a module.resource-type structure - this would allow you to find all the links to a particular module, and then more specific links to a given resource.

Postel's law is the way here: Be conservative in what you do, be liberal in what you accept from others.

As of Monday 2019-01-28, we no longer include an expanded list of licenseLinks in the returned license object. There may be 10s of 000s of license links and this would be a 
heavy payload to drop on an unsuspecting client. Clients will therefore need to query the /licenses/licenseLinks endpoint and filter by owner==UUID_OF_LICENSE in order to enumerate
the list of things a license might be linked to. This will give you full control over pagination, but prevent you from easily getting hold of the linked items in a default call.

## /licenses resource

The /licenses resource allows module clients to Create, Retrieve, Update and Delete license entities, and to search for licenses. [See the documentation](./docs/license_resource.md)

## /licenseLinks resource

/licenseLinks allows service clients to list all the resources that a license is connected to. N.B. This only reports links that mod-licenses manages, not inbound resources.

## Module installation and upgrade notes

The module has important dependences on reference data. initial installations and module upgrades should specify loadReference=true. The module
may not work as expected if this is omitted.

## ModuleDescriptor

https://github.com/folio-org/mod-licenses/blob/master/service/src/main/okapi/ModuleDescriptor-template.json

# For developers wanting to maintain and extend mod-licenses

## Regenerating the liquibase migrations script

grails -Dgrails.env=vagrant-db dbm-generate-gorm-changelog my-new-changelog.groovy

## Running using grails run-app with the vagrant provided postgres

grails -port 8081 -Dgrails.env=vagrant-db run-app

## Integration Tests

You will need to manually set up a postgres database user to hold the different test schemas and tenants. If using the vagrant backend box, use the following

    vagrant ssh
    sudo su - root
    sudo su - postgres
    psql
    CREATE DATABASE okapi_modules_test;
    GRANT ALL PRIVILEGES ON DATABASE okapi_modules_test to folio_admin;

This will configure an okapi_modules_test database and grant access to the folio_admin user. If you're using a local postgres setup, you will need the
emulate the above in your local config.

## Troubleshooting

This module runs on port 8081 when run from the run_external_reg.sh script, and this port is the assumed default for the deployment descriptor. This is so that
module developers can run mod_erm and mod_licenses side by side in development mode.

## Additional information

### Issue tracker

See project [MODLIC](https://issues.folio.org/browse/MODLIC)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker).

## Kubernetes Deployment Notes

You may need to set OKAPI_SERVICE_PORT and/or OKAPI_SERVICE_HOST on the mod-licenses container.

If you create a Service in Kubernetes named "okapi" and expose a port for Hazelcast and a port for HTTP, the Hazelcast port might be the default and mod-agreements will use that default.

When this happens you will see an exception such as `REST API is not enabled`. Setting the above environment variables will correct this.







