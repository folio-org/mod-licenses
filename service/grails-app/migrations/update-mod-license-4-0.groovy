databaseChangeLog = {
  // NEW STRUCTURE OF LICENSE ORG ROLES
  // new table license_org_role
  changeSet(author: "claudia (manual)", id: "202105261605-001") {
    createTable(tableName: "license_org_role") {
      column(name: "lior_id", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "lior_version", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "lior_role_fk", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "lior_owner_fk", type: "VARCHAR(36)") {
        constraints(nullable: "false")
      }

      column(name: "lior_note", type: "text")
    }

    addPrimaryKey(columnNames: "lior_id", constraintName: "license_org_rolePK", tableName: "license_org_role")

    addForeignKeyConstraint(baseColumnNames: "lior_role_fk", baseTableName: "license_org_role", constraintName: "li_org_role_refdata_valueFK", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "rdv_id", referencedTableName: "refdata_value")

    addForeignKeyConstraint(baseColumnNames: "lior_owner_fk", baseTableName: "license_org_role", constraintName: "li_org_role_sa_orgFK", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "sao_id", referencedTableName: "license_org")
  }

  // add boolean flag to license_org to indicate if the org is primary
  // default it to false
  changeSet(author: "claudia (manual)", id: "202105261606-001") {
    addColumn(tableName: "license_org") {
      column(name: "sao_primary_org", type: "boolean")
    }
  }

  changeSet(author: "claudia (manual)", id: "202105261607-001") {
    grailsChange {
      change {
	      sql.execute("""
	        UPDATE ${database.defaultSchemaName}.license_org SET sao_primary_org = FALSE
            WHERE sao_primary_org is null
	      """.toString())
      }
    }
  }

  changeSet(author: "claudia (manual)", id: "202105261608-001") {
    addNotNullConstraint(tableName: "license_org", columnName: "sao_primary_org", columnDataType: "boolean")
  }

  // in license_org set the primary_org to true if the org role is 'vendor'
  changeSet(author: "claudia (manual)", id: "202105261609-001") {
    grailsChange {
        change {
          sql.execute("UPDATE ${database.defaultSchemaName}.license_org SET sao_primary_org = TRUE WHERE sao_role in (SELECT rdv_id FROM ${database.defaultSchemaName}.refdata_value WHERE rdv_value='licensor')".toString())
        }
      }
  }

  changeSet(author: "claudia (manual)", id: "202105261610-002") {
    // Insert all roles from license_org for one license org in table license_org_role 
    // and leave only one entry
    // and remove the role column from license_org
    grailsChange {
      change {
        // Return the list of licenseIds and org Ids from license_org
        List<List<String>> licenseAndOrgIds = sql.rows("SELECT distinct sao.sao_owner_fk, sao.sao_org_fk FROM ${database.defaultSchemaName}.license_org as sao ORDER BY sao.sao_owner_fk".toString())
        licenseAndOrgIds.each {
          // For each of those subscriptionAgreement and orgIds, find out if there is an organization with role vendor attached
          def licensorId
          def transferId
          licensorId = sql.rows("SELECT sao.sao_id FROM ${database.defaultSchemaName}.license_org as sao WHERE sao.sao_owner_fk = :ownerId and sao.sao_org_fk = :orgId and sao.sao_role in (SELECT rdv_id FROM ${database.defaultSchemaName}.refdata_value WHERE rdv_value='licensor')".toString(), [ownerId: it.sao_owner_fk, orgId: it.sao_org_fk])
          if (licensorId) {
            transferId = licensorId
          } else {
            // find the first sao_id
            transferId = sql.rows("SELECT sao.sao_id FROM ${database.defaultSchemaName}.license_org as sao WHERE sao.sao_owner_fk = :ownerId and sao.sao_org_fk = :orgId limit 1".toString(), [ownerId: it.sao_owner_fk, orgId: it.sao_org_fk])
          }
          def saoId = transferId.sao_id.join()  // make a string out of the ArrayList
          // transfer lines with transferId.sao_id to license_org_role
          sql.execute("""
          INSERT INTO ${database.defaultSchemaName}.license_org_role(lior_id, lior_version, lior_owner_fk, lior_role_fk, lior_note)
          SELECT md5(random()::text || clock_timestamp()::text)::uuid as id, sao_version, :saoId, sao_role, sao_note FROM ${database.defaultSchemaName}.license_org WHERE sao_owner_fk = :ownerId and sao_org_fk = :orgId;
          """.toString(), [ownerId: it.sao_owner_fk, orgId: it.sao_org_fk, saoId: saoId])
          // keep only the line with the transferId, delete others from license_org
          sql.execute("""
          DELETE FROM ${database.defaultSchemaName}.license_org WHERE sao_owner_fk = :ownerId and sao_org_fk = :orgId AND sao_id != :saoId;
          """.toString(), [ownerId: it.sao_owner_fk, orgId: it.sao_org_fk, saoId: saoId])
          // empty the note field of the remaining line
          sql.execute("""
          UPDATE ${database.defaultSchemaName}.license_org SET sao_note = null WHERE sao_id = :saoId;
          """.toString(), [saoId: saoId])

        }
      }
    }
  }

  changeSet(author: "claudia (manual)", id: "202105261611-001") {
    dropForeignKeyConstraint(baseTableName: "license_org", constraintName: "FK1c9a0516d1bmdsb2afw6uxgtd")
  }

  changeSet(author: "claudia (manual)", id: "202105261612-001") {
    dropColumn(columnName: "sao_role", tableName: "license_org")
  }

  // Change category LicenseOrg.Role to 'not internal'
  changeSet(author: "claudia (manual)", id: "202105261613-001") {
    grailsChange {
      change {
        sql.execute("""
          UPDATE ${database.defaultSchemaName}.refdata_category SET internal = false
            WHERE rdc_description='LicenseOrg.Role'
        """.toString())
      }
    }
  }
}
