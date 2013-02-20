#!/bin/sh

# Requisitos:
#
# May need to run these commands before using this script:
#
# sudo apt-get install postgresql-client-common 
# sudo apt-get install postgresql-client-9.1

echo "=============================================="
echo "Creating database mySimpleCRM..."
echo
echo "Alert: It may be necessary to close any instance of postgreSQL as psql or pgadmin."
echo
echo "Message from console:"
echo
createdb -p 5432 -h localhost -U postgres -e mySimpleCRM
echo
echo "Finished Script!"
echo "=============================================="

