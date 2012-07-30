#!/bin/sh

# Requisitos:
#
# May need to run these commands before using this script:
#
# sudo apt-get install postgresql-client-common 
# sudo apt-get install postgresql-client-9.1


echo "=============================================="
echo "Deleting database MessengerConcurrent..."
echo
echo "Alert: Remember to close any instance of postgreSQL as psql or pgadmin."
echo
echo "Message from console:"
echo
dropdb -p 5432 -h localhost -U postgres -e messengerConcurrent
echo
echo "Finished Script!"
echo "=============================================="

