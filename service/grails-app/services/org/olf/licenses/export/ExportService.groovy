package org.olf.licenses.export
import org.hibernate.ScrollMode
import org.hibernate.ScrollableResults
import org.olf.licenses.License

import com.k_int.web.toolkit.custprops.CustomProperty
import com.k_int.web.toolkit.refdata.RefdataValue
import com.opencsv.ICSVWriter

import grails.gorm.multitenancy.CurrentTenant

/**
 * Service for exporting Licenses.
 */
@CurrentTenant
public class ExportService {
  /**
     * Payload shape:{
        ids: ["dd2037df-477d-40e5-9b71-73cdad1ad36f", "dd2037df-df77d-40e5-9b71-73cdad1ad36f"],
        include: {
          "name": true,
          "startDate": true,
          "endDate": true,
          "status": true,
          "customProperties": {
            "concurrentAccess": true,
          }
        }
        "terms: {
          "value": true,
          "internalNote": true,
          "PublicNote": true,
        }
      }
   */
  public exportLicensesAsCsv (final ICSVWriter csvWriter, final ExportControlObject exportData) {
    ScrollableResults results = null
    
    // Always write the header.
    writeHeader(csvWriter, exportData)
    
    if (exportData.ids) {
      ScrollMode f
      results = License.createCriteria().scroll {
        'in' 'id', exportData.ids
      }
      
      try {
        // Write each object in turn...
        while(results?.next()) {
          
          // Because we are projecting to beans we should just have 1
          // column that has the bean present.
          License license = results.get(0) as License
          writeLicenseAsCSV(csvWriter, license, exportData)
        }
      } finally {
      
        results.close()
      }
    }
  }
  
  private void writeHeader (final ICSVWriter csvWriter, final ExportControlObject exportData) {

    List<String> result = []
    for ( Map.Entry<String, Boolean> property : exportData.include ) {
      if (property.key != 'customProperties' && property.value == true) {
        result << property.key
      }
    }
    if (exportData.include?.customProperties) {
      for (Map.Entry<String, Boolean> custProp : exportData.include.customProperties as Map) {
        if (custProp.value == true) {
          // Output the property parts.
          for (String propPart : ExportControlObject.CUST_PROP_PART_ORDER) {
            
            if ( exportData.terms?.get(propPart) == true ) {
            
              if (propPart == 'value') {
                result << custProp.key
              } else {            
                result << ("${custProp.key}:${propPart}" as String)
              }
            }
          }
        }
      }
    }
    
    csvWriter.writeNext(result as String[])
  }
  
  private void writeLicenseAsCSV (final ICSVWriter csvWriter, final License license, final ExportControlObject exportData) {
    List<String> result = []
    for (final Map.Entry<String, ?> property : exportData.include) {
      if (property.key != 'customProperties' && property.value == true) {
        result << rootProperty ( license, property.key )
      }
    }
    
    if (exportData.include?.customProperties) {
      // Also output the terms.
      for (Map.Entry<String, Boolean> custProp : exportData.include.customProperties as Map) {
        if (custProp.value == true) {
          // Output the property.
          result.addAll( customProperty( license, custProp.key, exportData ) )
        }
      }
    }
    
    csvWriter.writeNext(result as String[])
  }
  
  private String rootProperty(final License license, final String propertyName) {
    handleValue (license[propertyName])
  }
  
  private List<String> customProperty(final License license, final String propertyName, final ExportControlObject exportData) {
    
    // Find the first instance.
    CustomProperty prop = license.customProperties?.value?.find( { CustomProperty cp -> cp.definition.name == propertyName} )
    
    // Output the property parts.
    List<String> result = []
    for (String propPart : ExportControlObject.CUST_PROP_PART_ORDER) {
      
      if ( exportData.terms?.get(propPart) == true ) {
        result << handleValue ( prop?.getAt(propPart))
      }
    }
    
    result
  }
  
  private String handleValue (def value) {
    
    if (value == null) {
      return ''
    }
    
    if (value instanceof RefdataValue) {
      
      return value.label
      
    } else if (!(value instanceof Collection)) {
      return "${value}"
    } else if (value instanceof Collection) {
      // Deal with multiValued custom properties by exporting a string of the form 'In progress;Active;Closed'
      def output = ''
      value.eachWithIndex {it, index ->
        output += "${index != 0 ? ';' : ''}${handleValue(it)}"
      }
      return output;
    }
    
    // Default to empty string.
    ''
  }
}
