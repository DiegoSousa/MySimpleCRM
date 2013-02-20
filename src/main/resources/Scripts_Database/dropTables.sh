#!/bin/sh

# Requisitos:
#
# May need to run these commands before using this script:
#
# sudo apt-get install postgresql-client-common 
# sudo apt-get install postgresql-client-9.1


echo "=============================================="
echo "Drop Tables from MySimpleCRM..."
echo
echo "Alert: It may be necessary to close any instance of postgreSQL as psql or pgadmin."
echo
echo "Message from console:"
echo
echo "Collecting informations to drop data in /tmp/droptables. Please provide a password if required: "
echo
psql -h localhost -U postgres -d mySimpleCRM -t -c "SELECT 'DROP TABLE ' || n.nspname || '.' || c.relname || ' CASCADE;' FROM pg_catalog.pg_class AS c LEFT JOIN pg_catalog.pg_namespace AS n ON n.oid = c.relnamespace WHERE relkind = 'r' AND n.nspname NOT IN ('pg_catalog', 'pg_toast') AND pg_catalog.pg_table_is_visible(c.oid)" > /tmp/droptables
echo
echo "Dropping all tables. Please provide a password if required: "
echo
psql -h localhost -d mySimpleCRM -U postgres -f /tmp/droptables
echo
echo "Finished Script!"
echo "=============================================="

