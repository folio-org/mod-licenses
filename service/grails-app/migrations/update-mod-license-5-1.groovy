databaseChangeLog = {
  changeSet(author: "julianladisch (manual)", id: "2023-12-04T18:12:00") {
		grailsChange {
			change {
				sql.execute('CREATE EXTENSION IF NOT EXISTS btree_gin WITH SCHEMA public;');
			}
		}
	}

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

	changeSet(author: "Jack_Golding (manual)", id: "20231123-004") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'document_attachment', columnNames: 'da_type_rdv_fk')
			}
		}
		createIndex(indexName: "document_attatchment_type_idx", tableName: "document_attachment") {
			column(name: "da_type_rdv_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231123-005") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'document_attachment', columnNames: 'da_type_rdv_fk')
			}
		}
		createIndex(indexName: "document_attatchment_type_idx", tableName: "document_attachment") {
			column(name: "da_type_rdv_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231123-006") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'alternate_name', columnNames: 'an_owner_fk')
			}
		}
		createIndex(indexName: "alternate_name_owner_idx", tableName: "alternate_name") {
			column(name: "an_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231123-007") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'internal_contact', columnNames: 'ic_owner_fk')
			}
		}
		createIndex(indexName: "internal_contact_owner_idx", tableName: "internal_contact") {
			column(name: "ic_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231123-008") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'internal_contact', columnNames: 'ic_user_fk')
			}
		}
		createIndex(indexName: "internal_contact_user_idx", tableName: "internal_contact") {
			column(name: "ic_user_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231123-009") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'license', columnNames: 'lic_type_rdv_fk')
			}
		}
		createIndex(indexName: "license_type_rdv_idx", tableName: "license") {
			column(name: "lic_type_rdv_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231123-0010") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'license', columnNames: 'am_owning_license_fk')
			}
		}
		createIndex(indexName: "license_amendment_owner_idx", tableName: "license") {
			column(name: "am_owning_license_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231123-0011") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'license_link', columnNames: 'll_owning_license_fk')
			}
		}
		createIndex(indexName: "license_link_owner_idx", tableName: "license_link") {
			column(name: "ll_owning_license_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231123-0014") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'license_org_role', columnNames: 'lior_owner_fk')
			}
		}
		createIndex(indexName: "license_org_role_owner_idx", tableName: "license_org_role") {
			column(name: "lior_owner_fk")
		}
	}

	changeSet(author: "Jack_Golding (manual)", id: "20231123-0015") {
		preConditions (onFail: 'MARK_RAN', onError: 'WARN') {
			not {
				indexExists(tableName: 'license_org_role', columnNames: 'lior_role_fk')
			}
		}
		createIndex(indexName: "license_org_role_role_idx", tableName: "license_org_role") {
			column(name: "lior_role_fk")
		}
	}
}
