databaseChangeLog = {

  // Put changes in here for 1.1.0
  changeSet(author: "sosguthorpe", id: "1549536544449-1") {
    addColumn (tableName: "license" ) {
      column(name: "lic_end_date", type: "timestamp")
    }
  }
  changeSet(author: "sosguthorpe", id: "1549536544449-2") {
    addColumn (tableName: "license" ) {
      column(name: "lic_start_date", type: "timestamp")
    }
  }

  changeSet(author: "Ian Ibbotson", id: "2019-02-11-0001") {
    addColumn(tableName: "license") {
      column(name: "lic_end_date_semantics_fk", type: "VARCHAR(36)")
    }
  }
}
