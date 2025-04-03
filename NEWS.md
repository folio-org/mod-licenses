## 6.1.4 2025-04-03
  * ERM-3666 Prevent instability of grails modules during updates: Ramsons

## 6.1.3 2025-01-24
  * ERM-3571 Add missing required interfaces to Module Descriptor

## 6.1.2 2024-12-03
  * ERM-3458 Add minio dependency in mod-licenses

## 6.1.1 2024-11-28
  * ERM-3441 Licenses cannot retrieve linked agreements due to permissions error
  * ERM-3420 Missing interface dependencies in module descriptor in mod-licenses

## 6.1.0 2024-10-30
  * ERM-3369 Update module license, guidance and dependencies for mod-licenses
  * ERM-3089 missing content-type header in /licenses/licenses/${uuid}/linkedAgreements response
  * Fix and automatically validate module descriptor (Eureka)

## 6.0.4 2024-10-18
  * ERM-3387 Description can be too long for index, causing mod-licenses error when updating from 5.0.x to later versions or failure on saving licenses with a long description

## 6.0.3 2024-07-05
  * ERM-3290 Fix permission on /licenses/licenses/{id}/linkedAgreements
  * ERM-3285 Fix permission on /licenses/files/{id}/raw in mod-licenses

## 6.0.2 2024-05-02
  * ERM-3208 Export license creates empty file

## 6.0.1 2024-04-17
  * ERM-3190 DB Connections are not being released
  * ERM-3175 Review outdated/vulnerable dependencies in mod-licenses

## 6.0.0 2024-03-22
  * ERM-3111 Upgrade Grails from 5 to 6
  * ERM-3073 Identify and implement indexes that should be added
    * All searchable elements have been assigned a GIN index
    * All foreign keys have been indexed

## 5.0.0 2023-10-11
  * ERM-3020 IsNotSet not working for custom properties
    * update to 'com.k_int.grails:web-toolkit-ce:8.1.1'
  * ERM-2993 Add amendments search in licenses app
  * ERM-2966 Error when match and filter contain the same path root
    * update to 'com.k_int.grails:web-toolkit-ce:8.1.0'
  * ERM-2961 Extend length of document URL to 2048 chars 
  * ERM-2940 spring-webmvc 5.3.25 security bypass vulnerability
  * ERM-2921 File Storage option missing in License application
  * ERM-2885 Reduce number of internal contact role defaults in Licenses
  * ERM-2642: Hibernate JPA Criteria SQL Injection (CVE-2020-25638)
    * update to 'org.hibernate:hibernate-core:5.4.28.Final'
    * update to 'org.hibernate:hibernate-java8:5.4.28.Final'
  * ERM-2641 Upgrade to Grails 5 (including Hibernate 5.6.x) for Poppy
  * ERM-2625 Add view only permissions for License settings

## 4.3.0 2023-02-20
  * ERM-2433 Upgrade postgresql, opencsv, minio, okhttp, kotlin
  * ERM-2226 Multi-pick list term values do not export with license

## 4.2.0 2022-06-29
  * ERM-2218 Removing values from multi-valued custom property in UI does not update the values
  * ERM-2201 Error on attempting to render agreement with a supplementary property
  * ERM-2124 Add multi-value support to custom properties
  * ERM-2070 mod-licenses Grails wrapper SAXParseException
  * ERM-2044 Ability to make custom properties deprecated
  * ERM-2043 Implement updated design for term management in license settings (Endpoint added to obtain all contexts on Terms)
  * ERM-2042 Duplicating agreement/license inc. documents moves files from original to new agreement/license
  * ERM-778 Add Date type to customProperties
  * Removed default perPage 100 in urlMappings for customPropertyDefinitions

## 4.1.0 2022-03-03
  * ERM-2037 Remove 'is not' option from Organization filter in Licenses simple search
  * ERM-1975 Script to insert and update /licenses/refdata entries
  * ERM-1945 Add AppSettings panel to Licenses
  * ERM-1943 Abstract and subclass existing toolkit FileObject (for Licenses)
  * ERM-1745 Add Organisations to Licenses simple search widget definition

## 4.0.0 2021-10-01
 * ERM-1849 Error on adding amendment to a license
 * ERM-1775 Regularly remove organizations that do not have any links to Licenses
 * ERM-1753 Migration to new Org / Org role structure leads to duplicate notes
 * ERM-1739 Remove duplicate stanzas from application.yml
 * ERM-1731 New licenses should have a default value for end date semantics
 * ERM-1543 Make organization roles for licenses editable in tenant settings
 * ERM-1541 Support for multiple roles per organisation in Licenses
 * ERM-1001 Separate permissions for file download in Licenses/Agreements

## 3.2.0 2021-06-15
 * ERM-1729 Is Set / Is Not Set doesn't work for Open Ended filter in license simple search widget
 * ERM-1728 Error on retrieving License CustomProperties
 * ERM-1696 Added match terms to WidgetDefinition
 * ERM-1653 Licenses SimpleSearch WidgetDefinition
 * ERM-1650 Add unique indexes for refdata tables
 * ERM-1643 Implemented dashboard interface

## 3.1.0 2021-03-15
 * FOLIO-2989 Remove duplicate licenses.licenseLinks.collection.get permission
 * ERM-1243 License Term values not duplicated on duplicating license
 * ERM-1229 Ability to duplicate Amendment
 * ERM-1204 It should not be possible to delete Licenses with attached Agreements
 * ERM-1201 Editing Amendment causes error
 * ERM-1188 For License CSV export use quote mark (ASCII 34) as escape character rather than backslash (ASCII 92)
 * ERM-972 Missing permission definition

## 3.0.0 2020-10-14
 * ERM-1046 Non-phrase searching support for licenses
 * ERM-967 Add "Note" to Organisation link in Licenses
 * ERM-904 Update tooling and framework
   * ERM-906 Update licenses to Grails 4
   * ERM-909 Update docker image to Java 11
 * ERM-828 Add support for "Alternative name" for licenses
 * ERM-975 Licenses: Use /organizations/ instead of /organizations-storage/
 * ERM-742 Custom properties: Backend validation not working

## 2.2.2 2020-06-25
 * ERM-970 500 status on when licenses have Documents without a file attachment

## 2.2.1 2020-06-23
 * ERM-841 Duplicate license: Files are not copied to the duplicated license record

## 2.2.0 2020-06-11
 * ERM-880 remove duplicate JSON fields in mod-licenses
 * ERM-828 Add support for "Alternative name" for licenses
 * ERM-814 Duplicate license
 * ERM-735 Separate refdata categories into "internal" and "user" lists
 * ERM-713 Produce license term comparison report
 * ERM-705 Support filtering licenses on the basis of terms being set/not set
 * ERM-668 Add ability to search/filter licenses based on custom properties (aka "terms")
 * ERM-193 Deleting a License (and possibly Agreement) with a Tag isn't possible

## 2.1.1 2020-04-01
 * ERM-783 Term descriptions are limited to 255 characters

## 2.1.0 2020-03-13
 * ERM-747 Custom Properties: Unable to correctly save decimals with german browser locale
 * ERM-675 mod-license upgrade from Q3.2 to Q4 fails
 * ERM-668 Add ability to search/filter licenses based on custom properties (aka "terms")
 * ERM-655 Sorting limits output in some cases
 * ERM-647 Sort custom properties alphabetically within order weights
 * ERM-591 Filters across custom properties do not work

## 2.0.1 2020-01-19
 * ERM-675 mod-license upgrade from Q3.2 to Q4 fails

## 2.0.0 2019-12-04
 * ERM-638 Use JVM features to manage container memory
 * ERM-538 Support health check endpoint (for example /admin/health provided by RMB)
 * ERM-505 Move test data so it's only active for the diku tenant only
 * ERM-477 License and agreement APIs are not protected by FOLIO permissions
   * ERM-478 Add permission definitions and api endpoint config
 * ERM-464 Agreements | Amendment with the most recent "Start date" takes precedence
 * ERM-436 Licenses | Provide user friendly error message on name field unique constraint
 * ERM-430 Display license and license amendment terms on agreement
 * ERM-419 Duplicate refdata entries in folio builds
 * ERM-417 /licenses/custprops cannot be sorted via the "sort" query param
 * ERM-362 Issue with calling install?purge=true option multiple times
 * ERM-297 File attachment over 10MB causes out of memory errors

## 1.11.0 2019-09-11
 * ERM-279 Provide integration tests for license creation covering start date, end date, status, type and end date semantics
   * ERM-369 Integration tests for license tags
   * ERM-368 Integration tests for license link
   * ERM-367 Integration tests for license endDateSemantics
   * ERM-366 Integration tests for license type
   * ERM-365 Integration tests for license status
   * ERM-364 Integration tests for license start-date
   * ERM-363 Integration tests for license end-date

## 1.10.0 2019-08-21
 * Re-release of 1.9.0 with updated module version

## 1.9.0 2019-08-12
 * ERM-357 Public and internal license term values
 * ERM-355 Manage public notes on license term values
 * ERM-274 Add cleanup task for orphan file uploads

## 1.8.0 2019-07-24
 * ERM-276 Investigate cause and impact of date/timezone issues
   * ERM-294 Echo date fixes within mod-licenses.

## 1.7.0 2019-06-11
* ERM-245 Tenant bootstrap improvements
  * ERM-247 Change descriptors to reflect new interface version
  * ERM-249 Create bootstrap data
* ERM-154 Set supplementary information for a licence amendment
* ERM-153 Manage core documents for an amendment
* ERM-144 Add note option to license custom properties
* ERM-147 Manage amendments for a license
  * ERM-93 Display amendment to a License
  * ERM-87 Remove amendment from a License
  * ERM-86 Edit amendment to a License
  * ERM-85 Add amendment to a License
* ERM-82 Support amendments for a license
  * ERM-138 Create amendment class
  * ERM-137 Create license abstraction
  * ERM-88 Add Amendment Domain model

## 1.6.0 2019-05-21
 * ERM-219 Support Organizations app as source of Organizations in Licenses
 * ERM-163 View internal contacts for a license
 * ERM-162 Manage internal contacts for a license
   * ERM-178 Save contact records to licenses
   * ERM-177 Add gson templates
   * ERM-176 Clone internal contact from agreements to licenses
 * ERM-92 	Require UUIDs that are RFC 4122 compliant
   * ERM-136 Dump all existing data on test environment
   * ERM-135 Change UUID generator from UUID to UUID-2

## 1.5.0 2019-05-07

 * ERM-166 Remove unwanted extra license section
 * ERM-133 Configure Document Categories
 * ERM-143 Add License / Supplementaty License Information Panel UI
 * ERM-181 Fix data sync issue with GOKb (Resumption Token and Broken Coverage)
 * ERM-139 Convert from SearchAndSort to SearchAndSortQuery
 * ERM-79 Set supplementary informaiton for a license
 * ERM-173 Manage Tags on Agreements
 * ERM-174 Seach Agreements by Tag
 * ERM-194 BUGFIX: Opening edit/create license with only one page does not work


## 1.4.0 2019-04-08

 * ERM-115 Provide correct data for agreement line
 * ERM-111 Build Settings Page
 * ERM-112 Build Wrapper Component for supression
 * ERM-113 Use Wrapper Component in Agreements
 * ERM-114 Write tests
 * ERM-98 Rendering Controlling Terms License
 * ERM-127 Resources with no coverage set should not display
 * ERM-110 Agreement Detail record - View attached EBSCO eResource
 * ERM-109 Support the ability to create an agreement from eHoldings
 * ERM-108 Supress agreements app functions
 * ERM-64 Show Controlling License Terms

## 1.3.0 2019-03-22
 * ERM-63 View linked agreement details in a license

## 1.2.0 2019-03-12
 * ERM-71 Add Model for JSON resource

 * ERM-37 Manage core documents for a license
 * ERM-69 Add DocumentAttachment Domain model
 * ERM-40 Remove a core document from a license
 * ERM-39 Edit license core document details
 * ERM-38 Add core documents to a License

 * ERM-7 Add an Organisation to a License
 * ERM-32 Add organization role validation to license to enforce no more than one Org per license with role:Licensor
 * ERM-25 Copy Organization structure from Agreements to Licenses
 * ERM-10 Remove an Organisation from a License
 * ERM-48 Make sure organizations can be removed from licenses in the backend
 * ERM-9 Change a license organisation's role

## 1.1.1 2019-02-23

 * ERM-1 eResource Managers can manually create licenses
 * ERM-6 Set pre-defined Terms for a License
 * ERM-7 Add an Organisation to a License
 * ERM-8 Add an Organisation to an existing License
 * ERM-10 Remove an Organisation from a License
 * ERM-11 eResource Managers can edit basic license details
 * ERM-12 Filter License Search Results by License Status
 * ERM-13 Filter License Search Results by License Type
 * ERM-16 Set open-ended License Expiry
 * ERM-17 See basic License details in search results
 * ERM-35 Filter Agreement Search Results by Organisation Role

## mod-licenses v1.1.0 released 2018-12-21

 * upgrade web-toolit dependencies, allow for override of DB parameters

## mod-licenses v1.0.1 released 2018-12-04

 * Jenkinsfile bugfix

## mod-licenses v1.0.0 released 2018-12-04

 * First release allowing licenese titles, custom properties / license terms and tags.
