databaseChangeLog = {
  changeSet(author: "claudia (manual)", id: "202006291730-1") {
    addColumn(tableName: "license_org") {
      column(name: "sao_note", type: "text")
    }
  }
}