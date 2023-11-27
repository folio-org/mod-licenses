databaseChangeLog = {
  changeSet(author: "Jack_Golding (manual)", id: "20231123-001") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'alternate_name', columnNames: 'an_name')
			}
		}
		grailsChange {
			change {
				def cmd = "CREATE INDEX alternate_name_name_idx ON ${database.defaultSchemaName}.alternate_name USING gin (an_name);".toString()
				sql.execute(cmd);
			}
		}
	}

  changeSet(author: "Jack_Golding (manual)", id: "20231123-002") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'license', columnNames: 'lic_name')
			}
		}
		grailsChange {
			change {
				def cmd = "CREATE INDEX license_name_idx ON ${database.defaultSchemaName}.license USING gin (lic_name);".toString()
				sql.execute(cmd);
			}
		}
	}

  changeSet(author: "Jack_Golding (manual)", id: "20231123-003") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'license', columnNames: 'lic_description')
			}
		}
		grailsChange {
			change {
				def cmd = "CREATE INDEX license_description_idx ON ${database.defaultSchemaName}.license USING gin (lic_description);".toString()
				sql.execute(cmd);
			}
		}
	}
}
