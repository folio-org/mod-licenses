databaseChangeLog = {
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
}
