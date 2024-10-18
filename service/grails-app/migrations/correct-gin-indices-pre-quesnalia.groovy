databaseChangeLog = {
  /* GIN Indexes have been added in update-mod-licenses-5-1.groovy
   * These can cause issues when the underlying data passes a certain limit
   * due to trigram operator not being used and the postgres token size growing
   *
   * It is a little unorthodox, however these index creations are conditional on
   * "CREATE IF NOT EXISTS". We will make use of that to insert indices on those
   * fields _before_ they are set up by upgrading/new implementors. For any tenants
   * who already managed to upgrade, these new changesets will be ignored, but it
   * it _recommended_ that an operational task be undertaken to bring those schemas
   * in line with the indices in place on new instances of mod-agreements
   */

  // The first two of these shouldn't be impacted by the bug outlined in ERM-3387, but we bring them in line anyway
  changeSet(author: "EFreestone (manual)", id: "2024-10-17-ERM-3387-quesnalia-1") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'alternate_name', columnNames: 'an_name')
			}
		}
		// Gin indexes need to be done via scripting.
		grailsChange {
			change {
				def cmd = "CREATE INDEX alternate_name_name_idx ON ${database.defaultSchemaName}.alternate_name USING gin (an_name gin_trgm_ops);".toString()
				sql.execute(cmd);
			}
		}
  }

  changeSet(author: "EFreestone (manual)", id: "2024-10-17-ERM-3387-quesnalia-2") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'license', columnNames: 'lic_name')
			}
		}
		grailsChange {
			change {
				def cmd = "CREATE INDEX license_name_idx ON ${database.defaultSchemaName}.license USING gin (lic_name gin_trgm_ops);".toString()
				sql.execute(cmd);
			}
		}
	}

  changeSet(author: "EFreestone (manual)", id: "2024-10-17-ERM-3387-quesnalia-3") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'license', columnNames: 'lic_description')
			}
		}
		grailsChange {
			change {
				def cmd = "CREATE INDEX license_description_idx ON ${database.defaultSchemaName}.license USING gin (lic_description gin_trgm_ops);".toString()
				sql.execute(cmd);
			}
		}
	}
}