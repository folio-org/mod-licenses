#!/bin/bash
# get most recent jar file name
the_jar_file=`ls build/libs/mod-licenses*.jar | tail -n 1`

echo Start mod-licenses in external-register mode using jarfile $the_jar_file

java -jar $the_jar_file -Xmx1G --server.port=8081 --grails.server.host=10.0.2.2 --dataSource.username=folio_admin --dataSource.password=folio_admin --dataSource.url=jdbc:postgresql://localhost:54321/okapi_modules

