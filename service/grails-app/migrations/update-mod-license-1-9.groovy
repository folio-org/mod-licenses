databaseChangeLog = {

    changeSet(author: "sosguthorpe (generated)", id: "1564401142529-1") {
        addColumn(tableName: "custom_property_definition") {
            column(name: "default_internal", type: "boolean") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "sosguthorpe (generated)", id: "1564401142529-2") {
        addColumn(tableName: "custom_property") {
            column(name: "internal", type: "boolean") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "sosguthorpe (generated)", id: "1564401142529-3") {
        addColumn(tableName: "custom_property") {
            column(name: "public_note", type: "text")
        }
    }
}