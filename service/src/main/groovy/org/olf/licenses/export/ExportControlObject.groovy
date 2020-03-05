package org.olf.licenses.export

import grails.databinding.BindUsing
import grails.databinding.BindingHelper
import grails.databinding.DataBindingSource

@BindUsing(ExportControlObject.Binder)
class ExportControlObject {
  
  public static final List<String> CUST_PROP_PART_ORDER = [ 'value', 'internal', 'note', 'publicNote' ] 
  
  List<String> ids = []
  LinkedHashMap<String, ?> include = [:] 
  Map<String, Boolean> terms = [:]
  
  private List<String> rootPropertyOrder = null
  List<String> getRootPropertyOrder() {
    if (rootPropertyOrder == null) {
      rootPropertyOrder = []
      if (include) {
        rootPropertyOrder.addAll(include.keySet().collect({ it != 'customProperties' }))
      }
    }
    
    rootPropertyOrder
  }
  
  private List<String> customPropertyOrder = null
  List<String> getCustomPropertyOrder() {
    if (rootPropertyOrder == null) {
      rootPropertyOrder = []
      if (include) {
        rootPropertyOrder.addAll(include.keySet())
      }
    }
  }
  
  private static class Binder implements BindingHelper {

    @Override
    public Object getPropertyValue(final Object obj, final String propertyName, final DataBindingSource source) {
      
      def propVal = obj[propertyName]
      
      switch (propertyName) {
        case "ids" :
          propVal.addAll(source.getPropertyValue(propertyName))
          break
        case 'include':
        case 'terms':
          propVal.putAll(source.getPropertyValue(propertyName))
          break
        default: 
          propVal = source.getPropertyValue(propertyName)
      }
      
      propVal
    }
    
  }
}
