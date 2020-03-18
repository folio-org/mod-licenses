databaseChangeLog = {
  changeSet(author: "efreestone (manual)", id: "202003171414-1") {
    modifyDataType(
      tableName: "custom_property_definition",
      columnName: "pd_description", type: "text",
      newDataType: "text",
      confirm: "Successfully updated the pd_description column."
    )
  }

  changeSet(author: "sosguthorpe (generated)", id: "1584554943442-1") {
    addColumn(tableName: "refdata_category") {
      column(name: "internal", type: "boolean")
    }
    addNotNullConstraint (tableName: "refdata_category", columnName: "internal", defaultNullValue: false)
  }
  
  changeSet(author: "sosguthorpe (generated)", id: "1584554943442-1") {
    addColumn(tableName: "refdata_category") {
      column(name: "internal", type: "boolean")
    }
    addNotNullConstraint (tableName: "refdata_category", columnName: "internal", defaultNullValue: false)
  }
  
  changeSet(author: "sosguthorpe (generated)", id: "1584554943442-2") {
    grailsChange {
      change {
        // Change all categories to internal where necessary. 
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.refdata_category SET internal = true
            WHERE rdc_description IN ('License.EndDateSemantics','License.Status','LicenseOrg.Role')
        """.toString())
      }
    }
  }
}
