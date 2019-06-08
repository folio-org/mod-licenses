
import org.olf.licenses.License

import com.k_int.web.toolkit.custprops.CustomPropertyDefinition
import com.k_int.web.toolkit.refdata.RefdataCategory
import com.k_int.web.toolkit.refdata.RefdataValue

if (existing_tenant) {
  log.info 'Skippping exisiting tenant'
  return
}

// Ensure the categories for the License properties.
RefdataValue.lookupOrCreate('Yes/No/Other', 'Yes')
RefdataValue.lookupOrCreate('Yes/No/Other', 'No')
RefdataValue.lookupOrCreate('Yes/No/Other', 'Other (see notes)')

RefdataValue.lookupOrCreate('Permitted/Prohibited', 'Permitted (explicit)')
RefdataValue.lookupOrCreate('Permitted/Prohibited', 'Permitted (explicit) under conditions')
RefdataValue.lookupOrCreate('Permitted/Prohibited', 'Permitted (interpreted)')
RefdataValue.lookupOrCreate('Permitted/Prohibited', 'Prohibited (explicit)')
RefdataValue.lookupOrCreate('Permitted/Prohibited', 'Prohibited (interpreted)')
RefdataValue.lookupOrCreate('Permitted/Prohibited', 'Unmentioned')
RefdataValue.lookupOrCreate('Permitted/Prohibited', 'Not applicable')

// Read the categories.
final String yesno = RefdataCategory.findByDesc('Yes/No/Other').id
final String permpro = RefdataCategory.findByDesc('Permitted/Prohibited').id

List propertyDefinitions =  [[
    "name" : "concurrentAccess",
    "type" : "Integer",
    "label" : "Number of concurrent users allowed",
    "description" : "The number of concurrent users allowed by the resource",
    "primary": true
  ],[
    "name" : "authorisedUsers",
    "type" : "Text",
    "label" : "Definition of authorised user",
    "description" : "The definition of an authorised user for a resource",
    "weight": -1,
    "primary": true
  ],[
    "name" : "walkInAccess",
    "category" : yesno,
    "type" : "Refdata",
    "label" : "Walk-in access permitted?",
    "description" : "Can non-members of the library/instittuion use the resource when in the library",
    "primary": true
  ],[
    "name" : "remoteAccess",
    "category" : yesno,
    "type" : "Refdata",
    "label" : "Access restricted to on-campus/campus network?",
    "description" : "Can access to the resource be provided from outside the library or institutional location / network",
    "primary": true
  ],[
    "name" : "illElectronic",
    "category" : permpro,
    "type" : "Refdata",
    "label" : "Electronic ILL",
    "description" : "The right to provide the licensed materials via interlibrary loan by way of electronic copies",
    "primary": true
  ],[
    "name" : "illSecureElectronic",
    "category" : permpro,
    "type" : "Refdata",
    "label" : "Secure Electronic ILL",
    "description" : "The right to provide the licensed materials via interlibrary loan by way of secure electronic transmission",
    "primary": true
  ],[
    "name" : "illPrint",
    "category" : permpro,
    "type" : "Refdata",
    "label" : "Print ILL",
    "description" : "The right to provide the licensed materials via interlibrary loan by way of print copies or facsimile transmission",
    "primary": true
  ],[
    "name" : "reservesElectronic",
    "category" : permpro,
    "type" : "Refdata",
    "label" : "Storage of electronic copies on secure network",
    "description" : "The right to make electronic copies of the licensed materials and store them on a secure network",
    "primary": true
  ],[
    "name" : "coursePackElectronic",
    "category" : permpro,
    "type" : "Refdata",
    "label" : "Use in electronic coursepacks",
    "description" : "The right to use licensed materials in collections or compilations of materials assembled in an electronic format by faculty members for use by students in a class for purposes of instruction",
    "primary": true
  ],[
    "name" : "coursePackPrint",
    "category" : permpro,
    "type" : "Refdata",
    "label" : "Use in print course packs",
    "description" : "The right to use licensed materials in collections or compilations of materials assembled in a print format by faculty members for use by students in a class for purposes of instruction",
    "primary": true
  ],[
    "name" : "copyDigital",
    "category" : permpro,
    "type" : "Refdata",
    "label" : "Making digital copies",
    "description" : "The right of the licensee and authorized users to download and digitally copy a reasonable portion of the licensed materials",
    "primary": true
  ],[
    "name" : "copyPrint",
    "category" : permpro,
    "type" : "Refdata",
    "label" : "Making print copies",
    "description" : "The right of the licensee and authorized users to print a reasonable portion of the licensed materials",
    "primary": true
  ],[
    "name" : "scholarlySharing",
    "category" : permpro,
    "type" : "Refdata",
    "label" : "Sharing for scholarly use",
    "description" : "The right of authorized users and/or licensee to transmit hard copy or electronic copy of reasonable amounts of licensed materials to a third party for personal, scholarly, educational, scientific or professional use"
  ],[
    "name" : "otherRestrictions",
    "type" : "Text",
    "label" : "Other restrictions",
    "description" : "Other restrictions expressed in the license"
  ],[
    "name" : "textAndDataMining",
    "category" : permpro,
    "type" : "Refdata",
    "label" : "Text and Data mining",
    "description" : "Whether it is permitted to use text and data mining processes on the content of the resource"
  ],[
    "name" : "metadataUsage",
    "type" : "Text",
    "label" : "Metadata usage",
    "description" : "Any restrictions expressed related to the use of metadata in the platforms"
  ],[
    "name" : "authIP",
    "category" : yesno,
    "type" : "Refdata",
    "label" : "IP authentication supported?",
    "description" : "Whether authentication via IP range is supported"
  ],[
    "name" : "authProxy",
    "category" : yesno,
    "type" : "Refdata",
    "label" : "Access via a proxy supported?",
    "description" : "Whether authentication via a reverse proxy is supported"
  ],[
    "name" : "postCancellationAccess",
    "category" : yesno,
    "type" : "Refdata",
    "label" : "Post-cancellation terms included?",
    "description" : "Does the license include post-cancellation terms?",
    "weight": 1
  ],[
    "name" : "authSAML",
    "category" : yesno,
    "type" : "Refdata",
    "label" : "SAML compliant authentication supported?",
    "description" : "Whether authentication via SAML compliant method is supported"
  ],[
    "name" : "annualOptOut",
    "category" : yesno,
    "type" : "Refdata",
    "label" : "Annual opt-out clause included?",
    "description" : "Whether the license includes an 'annual opt-out' clause within a multi-year agreement"
  ],[
    "name" : "APCAndOffsetting",
    "type" : "Text",
    "label" : "Whether the resource is subject to an APC discount or subscription cost offsetting agreement",
    "description" : "Whether the resource is subject to an APC discount or subscription cost offsetting agreement"
  ]
]

List licenseDefs = [
  [
    name: "Test License for tenant test2 should never make it in",
    description: "License for the AAAS Science Classic Agreement",
    status: "Active",
    type: "Local",
    startDate:"2019-01-01",
    openEnded:true,
    customProperties: [
      "walkInAccess": ["Yes"],
      "concurrentAccess": [10],
      "scholarlySharing": ["Prohibited (explicit)"],
      "textAndDataMining": ["Not applicable"],
      "remoteAccess": ["Yes"],
      "illElectronic": ["Prohibited (interpreted)"],
      "APCAndOffsetting": ["Yes"],
      postCancellationAccess: ["Yes"]
    ]
  ]
]

propertyDefinitions.each { Map definition ->
  final String type = definition.remove('type')
  CustomPropertyDefinition cpd = CustomPropertyDefinition.forType(type, definition)
  cpd.save(failOnError:true, flush: true)
}

licenseDefs.each { Map definition ->
  License l = new License()
  bindData(l, definition)
  l.save(failOnError: true, flush: true)
}


