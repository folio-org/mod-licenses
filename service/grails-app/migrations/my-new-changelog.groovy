databaseChangeLog = {

    changeSet(author: "malzer (generated)", id: "1554308605592-1") {
        createSequence(sequenceName: "hibernate_sequence")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-2") {
        createTable(tableName: "custom_property") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "custom_propertyPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "definition_id", type: "VARCHAR(36)")

            column(name: "parent_id", type: "BIGINT")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-3") {
        createTable(tableName: "custom_property_blob") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "OID") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-4") {
        createTable(tableName: "custom_property_boolean") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "BOOLEAN") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-5") {
        createTable(tableName: "custom_property_container") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-6") {
        createTable(tableName: "custom_property_decimal") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "NUMBER(19, 2)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-7") {
        createTable(tableName: "custom_property_definition") {
            column(name: "pd_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "pd_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pd_primary", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "pd_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pd_label", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "pd_description", type: "VARCHAR(255)")

            column(name: "pd_weight", type: "INT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-8") {
        createTable(tableName: "custom_property_integer") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "INT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-9") {
        createTable(tableName: "custom_property_refdata") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-10") {
        createTable(tableName: "custom_property_refdata_definition") {
            column(name: "pd_id", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "category_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-11") {
        createTable(tableName: "custom_property_text") {
            column(name: "id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "CLOB") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-12") {
        createTable(tableName: "document_attachment") {
            column(name: "da_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "da_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "da_date_created", type: "timestamp")

            column(name: "da_last_updated", type: "timestamp")

            column(name: "da_url", type: "VARCHAR(255)")

            column(name: "da_location", type: "VARCHAR(255)")

            column(name: "da_type_rdv_fk", type: "VARCHAR(36)")

            column(name: "da_name", type: "VARCHAR(255)")

            column(name: "da_note", type: "CLOB")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-13") {
        createTable(tableName: "license") {
            column(name: "lic_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "lic_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "lic_end_date_semantics_fk", type: "VARCHAR(36)")

            column(name: "lic_date_created", type: "timestamp")

            column(name: "custom_properties_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "lic_last_updated", type: "timestamp")

            column(name: "lic_end_date", type: "timestamp")

            column(name: "lic_start_date", type: "timestamp")

            column(name: "lic_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "lic_type_rdv_fk", type: "VARCHAR(36)")

            column(name: "lic_status_rdv_fk", type: "VARCHAR(36)")

            column(name: "lic_description", type: "CLOB")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-14") {
        createTable(tableName: "license_document_attachment") {
            column(name: "license_docs_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "document_attachment_id", type: "VARCHAR(36)")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-15") {
        createTable(tableName: "license_link") {
            column(name: "ll_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "ll_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "ll_label", type: "VARCHAR(255)")

            column(name: "ll_owning_license_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "direction", type: "VARCHAR(255)")

            column(name: "ll_relation", type: "VARCHAR(255)")

            column(name: "ll_remote_id", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-16") {
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

    changeSet(author: "malzer (generated)", id: "1554308605592-17") {
        createTable(tableName: "license_supp_doc") {
            column(name: "licsd_lic_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "licsd_da_fk", type: "VARCHAR(36)")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-18") {
        createTable(tableName: "license_tag") {
            column(name: "license_tags_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "tag_id", type: "BIGINT")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-19") {
        createTable(tableName: "org") {
            column(name: "org_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "org_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "org_vendors_uuid", type: "VARCHAR(255)")

            column(name: "org_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "org_source_uri", type: "VARCHAR(255)")

            column(name: "org_reference", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-20") {
        createTable(tableName: "refdata_category") {
            column(name: "rdc_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdc_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "rdc_description", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-21") {
        createTable(tableName: "refdata_value") {
            column(name: "rdv_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "rdv_value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_owner", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "rdv_label", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-22") {
        createTable(tableName: "tag") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "tagPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "norm_value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "value", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-23") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_blobPK", tableName: "custom_property_blob")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-24") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_booleanPK", tableName: "custom_property_boolean")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-25") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_containerPK", tableName: "custom_property_container")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-26") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_decimalPK", tableName: "custom_property_decimal")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-27") {
        addPrimaryKey(columnNames: "pd_id", constraintName: "custom_property_definitionPK", tableName: "custom_property_definition")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-28") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_integerPK", tableName: "custom_property_integer")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-29") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_refdataPK", tableName: "custom_property_refdata")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-30") {
        addPrimaryKey(columnNames: "pd_id", constraintName: "custom_property_refdata_definitionPK", tableName: "custom_property_refdata_definition")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-31") {
        addPrimaryKey(columnNames: "id", constraintName: "custom_property_textPK", tableName: "custom_property_text")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-32") {
        addPrimaryKey(columnNames: "da_id", constraintName: "document_attachmentPK", tableName: "document_attachment")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-33") {
        addPrimaryKey(columnNames: "lic_id", constraintName: "licensePK", tableName: "license")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-34") {
        addPrimaryKey(columnNames: "ll_id", constraintName: "license_linkPK", tableName: "license_link")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-35") {
        addPrimaryKey(columnNames: "sao_id", constraintName: "license_orgPK", tableName: "license_org")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-36") {
        addPrimaryKey(columnNames: "org_id", constraintName: "orgPK", tableName: "org")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-37") {
        addPrimaryKey(columnNames: "rdc_id", constraintName: "refdata_categoryPK", tableName: "refdata_category")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-38") {
        addPrimaryKey(columnNames: "rdv_id", constraintName: "refdata_valuePK", tableName: "refdata_value")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-39") {
        addUniqueConstraint(columnNames: "pd_name", constraintName: "UC_CUSTOM_PROPERTY_DEFINITIONPD_NAME_COL", tableName: "custom_property_definition")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-40") {
        createIndex(indexName: "rdv_entry_idx", tableName: "refdata_value") {
            column(name: "rdv_value")

            column(name: "rdv_owner")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-41") {
        createIndex(indexName: "td_label_idx", tableName: "custom_property_definition") {
            column(name: "pd_label")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-42") {
        createIndex(indexName: "td_primary_idx", tableName: "custom_property_definition") {
            column(name: "pd_primary")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-43") {
        createIndex(indexName: "td_type_idx", tableName: "custom_property_definition") {
            column(name: "pd_type")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-44") {
        createIndex(indexName: "td_weight_idx", tableName: "custom_property_definition") {
            column(name: "pd_weight")
        }
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-45") {
        addForeignKeyConstraint(baseColumnNames: "sao_role", baseTableName: "license_org", constraintName: "FK1c9a0516d1bmdsb2afw6uxgtd", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-46") {
        addForeignKeyConstraint(baseColumnNames: "licsd_lic_fk", baseTableName: "license_supp_doc", constraintName: "FK1iow3rhfnf9ehd19pawv3gxxa", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "lic_id", referencedTableName: "license")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-47") {
        addForeignKeyConstraint(baseColumnNames: "definition_id", baseTableName: "custom_property", constraintName: "FK36grvth72fb7wu5i5xaeqjitw", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "pd_id", referencedTableName: "custom_property_definition")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-48") {
        addForeignKeyConstraint(baseColumnNames: "value_id", baseTableName: "custom_property_refdata", constraintName: "FK5ogn0fedwxxy4fhmq9du4qej2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-49") {
        addForeignKeyConstraint(baseColumnNames: "document_attachment_id", baseTableName: "license_document_attachment", constraintName: "FK5taab61ws5rruo4ln7kvfw8sx", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "da_id", referencedTableName: "document_attachment")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-50") {
        addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "license_tag", constraintName: "FK8ityqdn37wc4tctg9d4mjxioq", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "tag")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-51") {
        addForeignKeyConstraint(baseColumnNames: "sao_org_fk", baseTableName: "license_org", constraintName: "FK8qke3qdgq9qmet11x25si7n8j", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "org_id", referencedTableName: "org")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-52") {
        addForeignKeyConstraint(baseColumnNames: "lic_type_rdv_fk", baseTableName: "license", constraintName: "FK9ci2rl35p8asb3ntcfwh4y2to", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-53") {
        addForeignKeyConstraint(baseColumnNames: "ll_owning_license_fk", baseTableName: "license_link", constraintName: "FKad55t1nq4dpdo8x043fgox9n2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "lic_id", referencedTableName: "license")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-54") {
        addForeignKeyConstraint(baseColumnNames: "category_id", baseTableName: "custom_property_refdata_definition", constraintName: "FKbrh88caagajlvrpaydg4tr3qx", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdc_id", referencedTableName: "refdata_category")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-55") {
        addForeignKeyConstraint(baseColumnNames: "parent_id", baseTableName: "custom_property", constraintName: "FKd5u2tgpracxvk1xw8pdreuj5h", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_property_container")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-56") {
        addForeignKeyConstraint(baseColumnNames: "license_docs_id", baseTableName: "license_document_attachment", constraintName: "FKfmy3990cbja9nn4n0wvo5owrj", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "lic_id", referencedTableName: "license")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-57") {
        addForeignKeyConstraint(baseColumnNames: "lic_status_rdv_fk", baseTableName: "license", constraintName: "FKfo7otg3447g3lig18jffflxhg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-58") {
        addForeignKeyConstraint(baseColumnNames: "sao_owner_fk", baseTableName: "license_org", constraintName: "FKg77bnpu94ffp3k06esc7klukl", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "lic_id", referencedTableName: "license")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-59") {
        addForeignKeyConstraint(baseColumnNames: "rdv_owner", baseTableName: "refdata_value", constraintName: "FKh4fon2a7k4y8b2sicjm0i6oy8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdc_id", referencedTableName: "refdata_category")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-60") {
        addForeignKeyConstraint(baseColumnNames: "lic_end_date_semantics_fk", baseTableName: "license", constraintName: "FKi0x1ooqy95qcd2y9xh2xh95n2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-61") {
        addForeignKeyConstraint(baseColumnNames: "licsd_da_fk", baseTableName: "license_supp_doc", constraintName: "FKjhd52agpa51dorj4b3h3c09tq", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "da_id", referencedTableName: "document_attachment")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-62") {
        addForeignKeyConstraint(baseColumnNames: "custom_properties_id", baseTableName: "license", constraintName: "FKkf3sdhtua5h6x9l6aw5mmv6xm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "custom_property_container")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-63") {
        addForeignKeyConstraint(baseColumnNames: "license_tags_id", baseTableName: "license_tag", constraintName: "FKrcsk9cvqiufe90gacx7gibs5u", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "lic_id", referencedTableName: "license")
    }

    changeSet(author: "malzer (generated)", id: "1554308605592-64") {
        addForeignKeyConstraint(baseColumnNames: "da_type_rdv_fk", baseTableName: "document_attachment", constraintName: "FKrggvdxk0jingkcnidb4hfwpi4", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }
}
