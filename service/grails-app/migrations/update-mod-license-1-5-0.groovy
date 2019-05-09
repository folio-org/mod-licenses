databaseChangeLog = {


    changeSet(author: "John Fereira", id: "2019-04-23-ERM-162-0") {
        createTable(tableName: "internal_contact") {
            column(name: "ic_id", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }

            column(name: "ic_version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "ic_role", type: "VARCHAR(36)")

            column(name: "ic_user_fk", type: "VARCHAR(255)")

            column(name: "ic_owner_fk", type: "VARCHAR(36)") {
                constraints(nullable: "false")
            }
        }
    }


    changeSet(author: "John Fereira", id: "2019-04-23-ERM-162-1") {
        addPrimaryKey(columnNames: "ic_id", constraintName: "internal_contactPK", tableName: "internal_contact")
    }


    changeSet(author: "John Fereira", id: "2019-04-23-ERM-162-2") {
        addForeignKeyConstraint(baseColumnNames: "ic_role", baseTableName: "internal_contact", constraintName: "FK_internal_contact_refdata_value", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")
    }

    changeSet(author: "John Fereira", id: "2019-04-23-ERM-162-3") {
        addForeignKeyConstraint(baseColumnNames: "ic_owner_fk", baseTableName: "internal_contact", constraintName: "FK_internal_contact_license", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "lic_id", referencedTableName: "license")
    }

}
