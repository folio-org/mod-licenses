databaseChangeLog = {

  changeSet(author: "sosguthorpe (generated)", id: "1574758603628-1") {
    createTable(tableName: "file_object") {
      column(name: "fo_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "file_contents", type: "OID") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "sosguthorpe (generated)", id: "1574758603628-2") {
    addColumn(tableName: "file_upload") {
      column(name: "file_object_id", type: "varchar(36)")
    }
  }

  changeSet(author: "sosguthorpe (generated)", id: "1574758603628-3") {
    addPrimaryKey(columnNames: "fo_id", constraintName: "file_objectPK", tableName: "file_object")
  }
  
  changeSet(author: "sosguthorpe (generated)", id: "1574758603628-4") {
    grailsChange {
      change {
        // First pre-populate all the referenced ids. 
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.file_upload SET file_object_id = md5(random()::text || clock_timestamp()::text)::uuid
        """.toString())
      }
    }
    grailsChange {
      change {
        
        // Insert the file data.
        sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.file_object (fo_id, version, file_contents)
            SELECT
              file_object_id as id,
              version,
              lo_from_bytea(0, fu_bytes)
            FROM 
              ${database.defaultSchemaName}.file_upload;
        """.toString())
      }
    }
    // Add the FKC once we have made sure we have inserted the data. This will cause the insert to rollback if we have made mistakes
    addForeignKeyConstraint(baseColumnNames: "file_object_id", baseTableName: "file_upload",
        constraintName: "FKmu4soh4lkppq4a7llu3k7yfqq", deferrable: "false",
        initiallyDeferred: "false", referencedColumnNames: "fo_id", referencedTableName: "file_object")
  }
  
  changeSet(author: "sosguthorpe (generated)", id: "1574758603628-5") {
    addNotNullConstraint (tableName: "file_upload", columnName: "file_object_id" )
  }
  
  changeSet(author: "sosguthorpe (generated)", id: "1574758603628-6") {
    dropColumn(columnName: "fu_bytes", tableName: "file_upload")
  }
}
