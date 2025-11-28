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

### Environment variables
This is a NON-EXHAUSTIVE list of environment variables which tweak behaviour in this module

| Variable                        | Description                                                                                                                                                                                                                                                                          | Options                                                                                                                                              | Default |
|---------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| `GLOBAL_S3_SECRET_KEY`          | Allows the setting of a global S3 secret key fallback. First module checks older S3SecretKey AppSetting. If not present then it falls back to this value.                                                                                                                            | S3 secret                                                                                                                                            |         |
| `ENDPOINTS_INCLUDE_STACK_TRACE` | Allows the HTTP response 500 to contain stacktrace from the exception thrown. Default return will be a generic message and a timestamp.                                                                                                                                              | <ul><li>`true`</li><li>`false`</li></ul>                                                                                                             | `false` |
| `DB_MAXPOOLSIZE`                | Configure connection pool to the PG instance for [HikariCP](https://github.com/brettwooldridge/HikariCP). This connection pool will be _doubled_ by each instance, to account for the "system" schema. Details below in the [connection pool issue section](#connection-pool-issues) | Integer. Recommended amount is 50 per instance if GOKB harvesting is configured (see [connection pool issue section](#connection-pool-issues) below) | 10      |

### Issues
This module has a few "problem" scenarios that _shouldn't_ occur in general operation, their history, reasoning and workarounds are documented below.
#### Locks and failure to upgrade
Particular approaches to upgrades in particular can leave the module unable to self right.
This occurs especially often where the module or container die/are killed regularly shortly after/during the upgrade.
The issue documented here was exacerbated by transaction handling changes brought about by the grails 5 -> 6 upgrade as part of Quesnalia, and fix attempts are ongoing.

In order of importance to check:

- **CPU resource**
    - In the past we have had these particular issues reported commonly where the app was not getting enough CPU resources to run. Please ensure that the CPU resources being allocated to the application are sufficient, see the requisite module requirements for the version running ([Ramsons example matrix](https://folio-org.atlassian.net/wiki/spaces/REL/pages/398983244/Ramsons+R2+2024+-+Bugfest+env+preparation+-+Modules+configuration+details?focusedCommentId=608305153))
- **Liquibase**
    - The module uses liquibase in order to facilitate module data migrations
    - Unfortunately this has a weakness to shutdown mid migration.
    - Check `<tenantName>_mod_licenses.tenant_changelog_lock` does not have `locked` set to `true`
        - If it does, the migration (and hence the upgrade itself) have failed, and it is difficult to extricate the module from this scenario.
        - It may be most prudent to revert the data and retry the upgrade.
    - In general, while the module is uploading it is most likely to succeed if after startup and tenant enabling/upgrading through okapi that the module and its container are NOT KILLED for at least 2 minutes.
    - An addition, a death to the module while upgrading could be due to a lack of reasonable resourcing making it to the module
- **Federated changelog lock**
    - The module also has a manual lock which is managed by the application itself.
    - This is to facilitate multiple instances accessing the same data
    - In particular, this lock table "seeds" every 20 minutes or so, and a death in the middle of this _can_ lock up the application (Although it can sometimes self right from here)
    - If the liquibase lock is clear, first try startup and leaving for a good 20 minutes
        - If the module dies it's likely resourcing that's the issue
        - The module may be able to self right
    - If the module cannot self right
        - Check the `mod_licenses__system.system_changelog_lock`
            - The same applies from the above section as this is a liquibase lock, but this is seriously unlikely to get caught as the table is so small
        - Finally check the `mod_licenses__system.federation_lock`
            - If this table has entries, this can prevent the module from any and all operations
            - It should self right from here, even if pointing at dead instances
                - See `mod_licenses__system.app_instance` for a table of instance ids, a killed and restarted module should eventually get cleared from here.
                - It is NOT RECOMMENDED to clear app_instances manually
            - If there are entries in the federated lock table that do not clear after 20 minutes of uninterrupted running then this table should be manually emptied.

#### Connection pool issues
As of Sunflower release, issues with [federated locks](#locks-and-failure-to-upgrade) and connection pools have been ongoing since Quesnalia.
The attempted fixes and history is documented in JIRA ticket [ERM-3851](https://folio-org.atlassian.net/browse/ERM-3851)

Initially the Grails 6 upgrade caused federated lock rows to themselves lock in PG.
A fix was made for Sunflower (v7.2.0) and backported to Quesnalia (v7.0.12) and Ramsons (v7.1.6).
However this fix is both not fully complete, and worsens an underlying connection pool issue.

The connection pool per instance can be configured via the `DB_MAXPOOLSIZE` environment variable.
Since the introduction of module-federation for this module, this has been _doubled_ to ensure connections are available
for the system schema as well. This is necessary as a starved system schema would all but guarantee the fed lock issues
documented above. As a response, our approach was to request more and more connections, memory, and CPU time to lower the
chances of this happening as much as possible.

As of right now, the recommended Sunflower connection pool is 10 per instance.
This leads to 20 connections per instance, almost all of which PG will see as idle. The non-dropping of idle connections
is a [chosen behaviour of Hikari](https://www.postgresql.org/message-id/1395487594923-5797135.post@n5.nabble.com) (and so not a bug).

At the moment, although postgres sees most of this pool as idle, Hikari internally believes them to be active, causing
pool starvation unless massively over-resourcing the instance. This in turn locks up the instance entirely and leads to jobs
silently failing

The workarounds here are to over-resource the module, and to restart problematic instances (or all instances)
when this behaviour manifests, or to revert to versions where this is less prevalent (v6.1.3, v6.2.0) and handle the
federated locking issues instead. Obviously these are not proper solutions.

In Trillium (v6.3.0), the aim is both to fix these bugs, and hopefully thus free up the connection pool to an extent that it can be
run with _significantly_ fewer connections, and potentially set up a way for the configured pool size to be mathematically
split between system and module, so as to avoid the doubling of the pool.

The recommendation for the versions containing the fix is to run with a minimum of 10 connections per instance
(Which will be doubled to 20 to account for the system schema).

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







