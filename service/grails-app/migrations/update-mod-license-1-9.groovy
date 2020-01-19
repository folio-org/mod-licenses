databaseChangeLog = {

    changeSet(author: "sosguthorpe (generated)", id: "1564401142529-1") {
      
      validCheckSum ("7:111ccc7d3f3d5941e70921eb2b1e2b59")
      
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
    
    changeSet(author: "sosguthorpe", id: "1564401142529-1-1") {
      
      // Re-run the last 2 statements above in a different set. This is to combat an error that
      // crept in when merging previous changesets. The above might have a differing version where the data is not moved.
      // Because we are treating either version of the above as valid, we have effectively 2 paths that could have gone before,
      // and this should ensure data correctness for either.
      
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
      
      validCheckSum ("7:6f685298406f837ede71bd020ad2ab90")
        
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

    changeSet(author: "sosguthorpe (generated)", id: "1564401142529-2-1") {
      
      // Re-run the last 2 statements above in a different set. This is to combat an error that
      // crept in when merging previous changesets. The above might have a differing version where the data is not moved.
      // Because we are treating either version of the above as valid, we have effectively 2 paths that could have gone before,
      // and this should ensure data correctness for either.
      
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