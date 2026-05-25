package org.olf.licenses.export

import grails.validation.Validateable
import groovy.transform.CompileStatic

@CompileStatic
class ExportControlObject implements Validateable {

  List<String> ids = []
  LinkedHashMap<String, ?> include = [:]
  LinkedHashMap<String, Boolean> terms = [:]

  /** Root License property keys to export, in caller order. */
  List<String> getOrderedRootKeys() {
    if (!include) return []
    List<String> result = []
    for (Map.Entry<String, ?> entry : include.entrySet()) {
      if (entry.key != 'customProperties' && entry.value == true) {
        result.add(entry.key)
      }
    }
    result
  }

  /** Custom-property keys to export, in caller order. */
  List<String> getOrderedCustomPropertyKeys() {
    Object raw = include?.get('customProperties')
    if (!(raw instanceof Map)) return []
    List<String> result = []
    for (Map.Entry<?, ?> entry : ((Map<?, ?>) raw).entrySet()) {
      if (entry.value == true) {
        result.add(entry.key as String)
      }
    }
    result
  }

  /** Term sub-columns to export, in caller order. */
  List<String> getOrderedTermParts() {
    if (!terms) return []
    List<String> result = []
    for (Map.Entry<String, Boolean> entry : terms.entrySet()) {
      if (entry.value == true) {
        result.add(entry.key)
      }
    }
    result
  }
}
