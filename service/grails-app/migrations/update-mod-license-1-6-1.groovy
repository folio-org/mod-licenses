databaseChangeLog = {
  changeSet(author: "John Fereira", id: "2019-05-14-ERM-218-1") {
    modifyDataType( 
      tableName: "custom_property_definition", 
      columnName: "pd_description", 
      newDataType: "TEXT",
      confirm: "Successfully updated pd_description column."
    )
  }

  changeSet(author: "John Fereira", id: "2019-05-14-ERM-218-2") {
    modifyDataType( 
      tableName: "license", 
      columnName: "lic_description", 
      newDataType: "TEXT",
      confirm: "Successfully updated lic_description column." 
    )
  }

  changeSet(author: "John Fereira", id: "2019-05-14-ERM-218-3") {
    modifyDataType( 
      tableName: "refdata_category", 
      columnName: "rdc_description", 
      newDataType: "TEXT",
      confirm: "Successfully updated rdc_description column." 
    )
  }

}
