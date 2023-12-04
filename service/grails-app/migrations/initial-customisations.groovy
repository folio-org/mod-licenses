databaseChangeLog = {
  changeSet(author: "sosguthorpe", id: "1") {
    grailsChange {
      change {
        // grailsChange gives us an sql variable which inherits the current connection, and hence should
        // get the schema
        // sql.execute seems to get a bit confused when passed a GString. Work it out before
        sql.execute('CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA public;');
      }
    }
  }
  changeSet(author: "julianladisch", id: "2023-12-04T18:12:00") {
    grailsChange {
      change {
        sql.execute('CREATE EXTENSION IF NOT EXISTS btree_gin WITH SCHEMA public;');
      }
    }
  }
}
