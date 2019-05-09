{
  "refdataCategories": {
    "yesno":{
      "desc":"Yes/No/Other", 
      "values":[
        { "value":"Yes", "label":"Yes" },
        { "value":"No", "label":"No" },
        { "value":"Other", "label":"Other (see notes)" }
      ]
    },
    "permittedprohibited": {
      "desc": "Permitted/Prohibited",
      "values": [
        { "label": "Permitted (explicit)" },
        { "label": "Permitted (explicit) under conditions" },
        { "label": "Permitted (interpreted)" },
        { "label": "Prohibited (explicit)" },
        { "label": "Prohibited (interpreted)" },
        { "label": "Unmentioned" },
        { "label": "Not applicable" }
      ]
    }
  },
  "propertyDefinitions" : [{
      "name" : "concurrentAccess",
      "type" : "Integer",
      "label" : "Number of concurrent users allowed",
      "description" : "The number of concurrent users allowed by the resource",
      "primary": true
    },{
      "name" : "authorisedUsers",
      "type" : "Text",
      "label" : "Definition of authorised user",
      "description" : "The definition of an authorised user for a resource",
      "weight": -1,
      "primary": true
    },{
      "name" : "walkInAccess",
      "category" : .refdataCategories.yesno.id,
      "type" : "Refdata",
      "label" : "Walk-in access permitted?",
      "description" : "Can non-members of the library/instittuion use the resource when in the library",
      "primary": true
    },{
      "name" : "remoteAccess",
      "category" : .refdataCategories.yesno.id,
      "type" : "Refdata",
      "label" : "Access restricted to on-campus/campus network?",
      "description" : "Can access to the resource be provided from outside the library or institutional location / network",
      "primary": true
    },{
      "name" : "illElectronic",
      "category" : .refdataCategories.permittedprohibited.id,
      "type" : "Refdata",
      "label" : "Electronic ILL",
      "description" : "The right to provide the licensed materials via interlibrary loan by way of electronic copies",
      "primary": true
    },{
      "name" : "illSecureElectronic",
      "category" : .refdataCategories.permittedprohibited.id,
      "type" : "Refdata",
      "label" : "Secure Electronic ILL",
      "description" : "The right to provide the licensed materials via interlibrary loan by way of secure electronic transmission",
      "primary": true
    },{
      "name" : "illPrint",
      "category" : .refdataCategories.permittedprohibited.id,
      "type" : "Refdata",
      "label" : "Print ILL",
      "description" : "The right to provide the licensed materials via interlibrary loan by way of print copies or facsimile transmission",
      "primary": true
    },{
      "name" : "reservesElectronic",
      "category" : .refdataCategories.permittedprohibited.id,
      "type" : "Refdata",
      "label" : "Storage of electronic copies on secure network",
      "description" : "The right to make electronic copies of the licensed materials and store them on a secure network",
      "primary": true
    },{
      "name" : "coursePackElectronic",
      "category" : .refdataCategories.permittedprohibited.id,
      "type" : "Refdata",
      "label" : "Use in electronic coursepacks",
      "description" : "The right to use licensed materials in collections or compilations of materials assembled in an electronic format by faculty members for use by students in a class for purposes of instruction",
      "primary": true
    },{
      "name" : "coursePackPrint",
      "category" : .refdataCategories.permittedprohibited.id,
      "type" : "Refdata",
      "label" : "Use in print course packs",
      "description" : "The right to use licensed materials in collections or compilations of materials assembled in a print format by faculty members for use by students in a class for purposes of instruction",
      "primary": true
    },{
      "name" : "copyDigital",
      "category" : .refdataCategories.permittedprohibited.id,
      "type" : "Refdata",
      "label" : "Making digital copies",
      "description" : "The right of the licensee and authorized users to download and digitally copy a reasonable portion of the licensed materials",
      "primary": true
    },{
      "name" : "copyPrint",
      "category" : .refdataCategories.permittedprohibited.id,
      "type" : "Refdata",
      "label" : "Making print copies",
      "description" : "The right of the licensee and authorized users to print a reasonable portion of the licensed materials",
      "primary": true
    },{
      "name" : "scholarlySharing",
      "category" : .refdataCategories.permittedprohibited.id,
      "type" : "Refdata",
      "label" : "Sharing for scholarly use",
      "description" : "The right of authorized users and/or licensee to transmit hard copy or electronic copy of reasonable amounts of licensed materials to a third party for personal, scholarly, educational, scientific or professional use"
    },{
      "name" : "otherRestrictions",
      "type" : "Text",
      "label" : "Other restrictions",
      "description" : "Other restrictions expressed in the license"
    },{
      "name" : "textAndDataMining",
      "category" : .refdataCategories.permittedprohibited.id,
      "type" : "Refdata",
      "label" : "Text and Data mining",
      "description" : "Whether it is permitted to use text and data mining processes on the content of the resource"
    },{
      "name" : "metadataUsage",
      "type" : "Text",
      "label" : "Metadata usage",
      "description" : "Any restrictions expressed related to the use of metadata in the platforms"
    },{
      "name" : "authIP",
      "category" : .refdataCategories.yesno.id,
      "type" : "Refdata",
      "label" : "IP authentication supported?",
      "description" : "Whether authentication via IP range is supported"
    },{
      "name" : "authProxy",
      "category" : .refdataCategories.yesno.id,
      "type" : "Refdata",
      "label" : "Access via a proxy supported?",
      "description" : "Whether authentication via a reverse proxy is supported"
    },{
      "name" : "postCancellationAccess",
      "category" : .refdataCategories.yesno.id,
      "type" : "Refdata",
      "label" : "Post-cancellation terms included?",
      "description" : "Does the license include post-cancellation terms?",
      "weight": 1
    },{
      "name" : "authSAML",
      "category" : .refdataCategories.yesno.id,
      "type" : "Refdata",
      "label" : "SAML compliant authentication supported?",
      "description" : "Whether authentication via SAML compliant method is supported"
    },{
      "name" : "annualOptOut",
      "category" : .refdataCategories.yesno.id,
      "type" : "Refdata",
      "label" : "Annual opt-out clause included?",
      "description" : "Whether the license includes an 'annual opt-out' clause within a multi-year agreement"
    },{
      "name" : "APCAndOffsetting",
      "type" : "Text",
      "label" : "Whether the resource is subject to an APC discount or subscription cost offsetting agreement",
      "description" : "Whether the resource is subject to an APC discount or subscription cost offsetting agreement"
    }
  ],
  "licenseDefs": [
    {
      name: "American Association for the Advancement of Science/NESLi2/Science Classic/2014-2114",
      description: "License for the AAAS Science Classic Agreement",
      status: "Active",
      type: "Local",
      startDate:"2019-01-01",
      openEnded:true,
      customProperties: {
        "walkInAccess": ["Yes"],
        "concurrentAccess": [10],
        "scholarlySharing": ["Prohibited (explicit)"],
        "textAndDataMining": ["Not applicable"],
        "remoteAccess": ["Yes"],
        "illElectronic": ["Prohibited (interpreted)"],
        "APCAndOffsetting": ["Yes"],
        postCancellationAccess: ["Yes"]
      },
      docs:[
        { "name": "A test document attachment", "location":"http://a.b.c/d/e/f.doc", "url":"http://a.b.c/d", "note":"This is a document attachment",  "atType":"License" },
        { "name": "A test document attachment", "location":"http://a.b.c/d/e/g.doc", "url":"http://a.b.c/e", "note":"This is a second document attachment",  "atType":"Misc" }
      ],
      supplementaryDocs:[
        { "name": "A test SD document attachment 1", "location":"http://a.b.c/d/e/f.doc", "note":"This is a document attachment",  "atType":"License" },
        { "name": "A test SD document attachment 2", "location":"http://a.b.c/d/e/g.doc", "note":"This is a second document attachment",  "atType":"Misc" }
      ]
    },
    {
      name: "Test License 001",
      status: "Active",
      type: "Local",
      description: "This is a test licenses",
      startDate:"2019-01-1",
      endDate:"2020-01-01",
      endDateSemantics:"Explicit",
      customProperties: {
        "walkInAccess": "Yes",
        "concurrentAccess": 10,
        "scholarlySharing": "Permitted (explicit)",
        "textAndDataMining": "Permitted (explicit) under conditions",
        "remoteAccess": "Yes",
        "illElectronic": "Unmentioned",
        "APCAndOffsetting": "Yes",
        "postCancellationAccess": "No"
      },
      "tags": [
        "legacy"
      ],
      "links": [
        {
          "linkType": "kb-ebsco.package", 
          "linkId": "12-334", 
          "linkLabel": "An example link to an EKB or eholdings defined package", 
          "relation": "describes",
          "direction": "out"
        }
      ],
      "orgs": [
        { 
          "role": "Licensor", 
          "org": { 
            "name": "EBSCO", 
            "orgsUuid": "1234-5678-9102-3356"
          } 
        },
        { 
          "role": "Licensee", 
          "org": { 
            "name": "Some Content Provider", 
            "orgsUuid": "1234-5678-9102-3355"
          } 
        }
      ]
    },
    {
      name: "Test License 002",
      status: "Active",
      type: "Local",
      tags: [
        "legacy"
      ]
    },
    {
      name: "Test License 003",
      tags: [
        "Test4"
      ]
    },
    {
      name: "BMJ Journals Online 2011-2012 NESLi2",
      status: "Expired",
      type: "Consortial",
      openEnded: false,
      endDate: "2019-12-31",
      tags: [
        "Test5"
      ]
    },
    {
      name: "Academic Rights Press/Test Consortium/InteLex Collections - Perpetual Purchase Agreement",
      tags: [
        "Test6"
      ],
      description: "This is a test case for a really long description. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    },
    {
      name: "An invalid license with 2 licensor orgs",
      tags: [
        "Test7"
      ],
      description: "This record should not be created",
      orgs:[
        {
         "role": "Licensor",
          "org": { "name": "Test1", "orgsUuid": "1234-5678-9102-3357" }
        },
        {
          "role": "Licensor",
          "org": { "name": "Test2", "orgsUuid": "1234-5678-9102-3358" }
        }
      ]
    }
  ]
}
