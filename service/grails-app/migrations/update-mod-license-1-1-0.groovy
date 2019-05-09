databaseChangeLog = {
  changeSet(author: "Ian Ibbotson", id: "2019-02-11-0001") {
    addColumn(tableName: "license") {
      column(name: "lic_end_date_semantics_fk", type: "VARCHAR(36)")
    }
  }
  
  changeSet(author: "Steve Osguthorpe", id: "2019-02-13-ERM-6") {
    addColumn(tableName: "custom_property_definition") {
      column (name: "pd_label", type: "VARCHAR(255)") 
      column(name: "pd_weight", type: "INT")
    }
    
    // Set all labels.
    grailsChange {
      change {
        // Create default values for the labels.
        sql.execute("UPDATE ${database.defaultSchemaName}.custom_property_definition SET pd_label = REGEXP_REPLACE(pd_name,'([a-z0-9A-Z])([A-Z][a-z])','\\1 \\2','g');".toString())
      }
    }
    
    // Add the constraints after adding the data.
    addNotNullConstraint (tableName: "custom_property_definition", columnName: "pd_label" )
    addNotNullConstraint (tableName: "custom_property_definition", columnName: "pd_weight", defaultNullValue: 0)
  }

  changeSet(author: "ibbo (generated)", id: "2019-02-19-0001") {
    createTable(tableName: "license_org") {
      column(name: "sao_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "sao_version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "sao_org_fk", type: "VARCHAR(36)")

      column(name: "sao_role", type: "VARCHAR(36)")

      column(name: "sao_owner_fk", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "ibbo (generated)", id: "2019-02-19-0002") {
    createTable(tableName: "org") {
      column(name: "org_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "org_version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "org_orgs_uuid", type: "VARCHAR(255)")

      column(name: "org_name", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "ibbo (generated)", id: "2019-02-19-0003") {
    addPrimaryKey(columnNames: "sao_id", constraintName: "license_orgPK", tableName: "license_org")
  }

  changeSet(author: "ibbo (generated)", id: "2019-02-19-0004") {
    addPrimaryKey(columnNames: "org_id", constraintName: "orgPK", tableName: "org")
  }

  changeSet(author: "ibbo (generated)", id: "2019-02-19-0005") {
    addForeignKeyConstraint(baseColumnNames: "sao_org_fk", baseTableName: "license_org", constraintName: "FK8qke3qdgq9qmet11x25si7n8j", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "org_id", referencedTableName: "org")
  }

  changeSet(author: "ibbo (generated)", id: "2019-02-19-0006") {
    addForeignKeyConstraint(baseColumnNames: "sao_owner_fk", baseTableName: "license_org", constraintName: "FKg77bnpu94ffp3k06esc7klukl", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "lic_id", referencedTableName: "license")
  }

  changeSet(author: "ibbo (generated)", id: "2019-02-19-0007") {
    addForeignKeyConstraint(baseColumnNames: "sao_role", baseTableName: "license_org", constraintName: "FK1c9a0516d1bmdsb2afw6uxgtd", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
  }

  changeSet(author: "sosguthorpe (generated)", id: "2019-02-19-ERM-6") {
    addColumn(tableName: "custom_property_definition") {
      column(name: "pd_primary", type: "BOOLEAN")
    }
    addNotNullConstraint (tableName: "custom_property_definition", columnName: "pd_primary", defaultNullValue: 'FALSE')
  }
}
