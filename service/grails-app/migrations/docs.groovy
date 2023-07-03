databaseChangeLog = {

  changeSet(author: "ibbo (generated)", id: "1551116551649-12") {
    createTable(tableName: "document_attachment") {
      column(name: "da_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }
      column(name: "da_version", type: "BIGINT") {
        constraints(nullable: "false")
      }
      column(name: "da_date_created", type: "timestamp")
      column(name: "da_last_updated", type: "timestamp")
      column(name: "da_location", type: "VARCHAR(255)")
      column(name: "da_type_rdv_fk", type: "VARCHAR(36)")
      column(name: "da_name", type: "VARCHAR(255)")
      column(name: "da_note", type: "CLOB")
    }
  }

  changeSet(author: "ibbo (generated)", id: "1551116551649-14") {
    createTable(tableName: "license_document_attachment") {
      column(name: "license_docs_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }
      column(name: "document_attachment_id", type: "VARCHAR(36)")
    }
  }

  changeSet(author: "ibbo (generated)", id: "1551116551649-31") {
    addPrimaryKey(columnNames: "da_id", constraintName: "document_attachmentPK", tableName: "document_attachment")
  }

  changeSet(author: "ibbo (generated)", id: "1551116551649-47") {
    addForeignKeyConstraint(baseColumnNames: "document_attachment_id", baseTableName: "license_document_attachment", constraintName: "FK5taab61ws5rruo4ln7kvfw8sx", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "da_id", referencedTableName: "document_attachment")
  }

  changeSet(author: "ibbo (generated)", id: "1551116551649-54") {
    addForeignKeyConstraint(baseColumnNames: "license_docs_id", baseTableName: "license_document_attachment", constraintName: "FKfmy3990cbja9nn4n0wvo5owrj", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "lic_id", referencedTableName: "license")
  }

  changeSet(author: "ibbo (manual)", id: "2019-02-28-00001") {
    createTable(tableName: "license_supp_doc") {
      column(name: "licsd_lic_fk", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }
      column(name: "licsd_da_fk", type: "VARCHAR(36)")
    }
  }

  // Foreign key constraints for external license documents
  changeSet(author: "ibbo (manual)", id: "2019-02-28-00002") {
    addForeignKeyConstraint(baseColumnNames: "licsd_lic_fk",
                            baseTableName: "license_supp_doc",
                            constraintName: "licsd_to_sa_fk",
                            deferrable: "false", initiallyDeferred: "false",
                            referencedColumnNames: "lic_id",
                            referencedTableName: "license")
  }

  changeSet(author: "ibbo (manual)", id: "2019-02-28-00003") {
    addForeignKeyConstraint(baseColumnNames: "licsd_da_fk",
                            baseTableName: "license_supp_doc",
                            constraintName: "licsd_to_da_fk",
                            deferrable: "false", initiallyDeferred: "false",
                            referencedColumnNames: "da_id",
                            referencedTableName: "document_attachment")
  }

  changeSet(author: "ibbo (manual)", id: "20190226-002") {
    addColumn (tableName: "document_attachment" ) {
      column(name: "da_url", type: "varchar(255)")
    }
  }

  // extend document_attachment's url field to 2048 characters
  changeSet(author: "Claudia (manual)", id: "20230703-001") {
    modifyDataType (
      tableName: "document_attachment",
      columnName: "da_url",
      newDataType: "varchar(2048)")
  }
}
