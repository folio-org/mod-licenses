psql -U folio_admin -h localhost okapi_modules

set search_path to diku_mod_licenses, public;

select * from license;




# nuke custprops

delete from custom_property_blob;
delete from custom_property_boolean;
delete from custom_property_container;
delete from custom_property_decimal;
delete from custom_property_integer;
delete from custom_property_refdata;
delete from custom_property_refdata_definition;
delete from custom_property_text;
delete from custom_property;
delete from custom_property_definition;
