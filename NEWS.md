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
