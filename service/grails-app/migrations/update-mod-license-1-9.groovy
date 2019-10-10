databaseChangeLog = {

    changeSet(author: "sosguthorpe (generated)", id: "1564401142529-1") {
      addColumn(tableName: "custom_property_definition") {
        column (name: "default_internal", type: "boolean")
      }
      
      // Combine foreign key references into single column.
      grailsChange {
        change {
          // Create default values for the labels.
          sql.execute("UPDATE ${database.defaultSchemaName}.custom_property_definition SET default_internal=True WHERE default_internal IS NULL;".toString())
        }
      }
      
      // Add the constraints after adding the data.
      addNotNullConstraint (tableName: "custom_property_definition", columnName: "default_internal" )
    }

    changeSet(author: "sosguthorpe (generated)", id: "1564401142529-2") {
        
      addColumn(tableName: "custom_property") {
        column (name: "internal", type: "boolean")
      }
      
      // Combine foreign key references into single column.
      grailsChange {
        change {
          // Create default values for the labels.
          sql.execute("UPDATE ${database.defaultSchemaName}.custom_property SET internal=True WHERE internal IS NULL;".toString())
        }
      }
      
      // Add the constraints after adding the data.
      addNotNullConstraint (tableName: "custom_property", columnName: "internal" )
    }

    changeSet(author: "sosguthorpe (generated)", id: "1564401142529-3") {
        addColumn(tableName: "custom_property") {
            column(name: "public_note", type: "text")
        }
    }
}