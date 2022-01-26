databaseChangeLog = {

  changeSet(author: "ianibbo (manual)", id: "202109161336-002") {
    dropNotNullConstraint(columnName: "file_contents", tableName: "file_object")

    addColumn (tableName: "file_object" ) {
      column(name: "class", type: "VARCHAR(255)")
      column(name: "fo_s3ref", type: "VARCHAR(255)")
    }
  }

  changeSet(author: "ianibbo (manual)", id: "202109161336-003") {
    grailsChange {
      change {
        sql.execute("UPDATE ${database.defaultSchemaName}.file_object SET class = 'DB' where class is null".toString());
      }
    }
  }

  changeSet(author: "ianibbo (manual)", id: "202201261047-001") {
    addColumn (tableName: "custom_property_definition" ) {
      column(name: "pd_ctx", type: "VARCHAR(255)")
    }
  }
}
