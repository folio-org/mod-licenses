databaseChangeLog = {
  changeSet(author: "sosguthorpe (generated)", id: "1579605191629-1") {
    modifyDataType (tableName: 'custom_property_integer', columnName: 'value', newDataType: 'bigint')
  }

  changeSet(author: "efreestone (manual)", id: "202002271345-1") {
    addColumn(tableName: "license") {
      column(name: "lic_local_reference", type: "varchar(128)")
    }
  }
}
