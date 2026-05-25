package org.olf.licenses.export

import spock.lang.Specification

class ExportControlObjectSpec extends Specification {

  void 'getOrderedRootKeys preserves caller iteration order'() {
    given:
    ExportControlObject obj = new ExportControlObject()
    obj.include = [
      endDate  : true,
      name     : true,
      type     : true,
      startDate: true,
      status   : true,
    ] as LinkedHashMap

    expect:
    obj.orderedRootKeys == ['endDate', 'name', 'type', 'startDate', 'status']
  }

  void 'getOrderedRootKeys skips customProperties and entries whose value is not true'() {
    given:
    ExportControlObject obj = new ExportControlObject()
    obj.include = [
      name            : true,
      description     : false,
      type            : true,
      customProperties: [foo: true] as LinkedHashMap,
    ] as LinkedHashMap

    expect:
    obj.orderedRootKeys == ['name', 'type']
  }

  void 'getOrderedRootKeys returns empty list for empty include'() {
    expect:
    new ExportControlObject().orderedRootKeys == []
  }

  void 'getOrderedRootKeys returns empty list for null include'() {
    given:
    ExportControlObject obj = new ExportControlObject()
    obj.include = null

    expect:
    obj.orderedRootKeys == []
  }

  void 'getOrderedCustomPropertyKeys preserves caller iteration order'() {
    given:
    ExportControlObject obj = new ExportControlObject()
    obj.include = [
      customProperties: [
        remoteAccess    : true,
        illElectronic   : true,
        concurrentAccess: true,
        illNotes        : true,
      ] as LinkedHashMap,
    ] as LinkedHashMap

    expect:
    obj.orderedCustomPropertyKeys == ['remoteAccess', 'illElectronic', 'concurrentAccess', 'illNotes']
  }

  void 'getOrderedCustomPropertyKeys skips custom properties whose value is not true'() {
    given:
    ExportControlObject obj = new ExportControlObject()
    obj.include = [
      customProperties: [
        concurrentAccess: true,
        remoteAccess    : false,
        illNotes        : true,
      ] as LinkedHashMap,
    ] as LinkedHashMap

    expect:
    obj.orderedCustomPropertyKeys == ['concurrentAccess', 'illNotes']
  }

  void 'getOrderedCustomPropertyKeys returns empty list when include is empty'() {
    expect:
    new ExportControlObject().orderedCustomPropertyKeys == []
  }

  void 'getOrderedCustomPropertyKeys returns empty list when include has no customProperties'() {
    given:
    ExportControlObject obj = new ExportControlObject()
    obj.include = [name: true] as LinkedHashMap

    expect:
    obj.orderedCustomPropertyKeys == []
  }

  void 'getOrderedCustomPropertyKeys returns empty list when customProperties is not a Map'() {
    given:
    ExportControlObject obj = new ExportControlObject()
    obj.include = [customProperties: true] as LinkedHashMap

    expect:
    obj.orderedCustomPropertyKeys == []
  }

  void 'getOrderedTermParts preserves caller order and filters out false'() {
    given:
    ExportControlObject obj = new ExportControlObject()
    obj.terms = [
      publicNote: true,
      value     : true,
      note      : false,
      internal  : true,
    ] as LinkedHashMap

    expect:
    obj.orderedTermParts == ['publicNote', 'value', 'internal']
  }

  void 'getOrderedTermParts returns empty list when terms is empty'() {
    expect:
    new ExportControlObject().orderedTermParts == []
  }
}