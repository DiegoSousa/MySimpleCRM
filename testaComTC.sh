#!/bin/sh

# /**
#  * Script to run test with ThreadControl.
#  *
#  * @author Ayla Rebouças - ayla@dce.ufpb.br
#  * @author Diego Sousa - diego.sousa@dce.ufpb.br
#  *
#  */

echo
echo "Enter the quantity of times that the test must be run:"
echo 
read quant
echo 

#echo "Enter the path to the folder .M2 of mavem or open this script and change the variable "'$HOME'" by path to the folder .M2"
#echo "Ex: If the path is /home/user/.m2 then enter with /home/user without o /.m2"
#echo 
#read pathM2
#echo

for i in `seq 1 $quant`
do    

java -cp :target/classes/:target/test-classes/:$HOME/.m2/repository/org/hibernate/hibernate-core/4.1.4.Final/hibernate-core-4.1.4.Final.jar:$HOME/.m2/repository/org/hibernate/hibernate-entitymanager/4.1.4.Final/hibernate-entitymanager-4.1.4.Final.jar:$HOME/.m2/repository/org/hibernate/common/hibernate-commons-annotations/4.0.1.Final/hibernate-commons-annotations-4.0.1.Final.jar:$HOME/.m2/repository/org/hibernate/javax/persistence/hibernate-jpa-2.0-api/1.0.1.Final/hibernate-jpa-2.0-api-1.0.1.Final.jar:$HOME/.m2/repository/antlr/antlr/2.7.7/antlr-2.7.7.jar:$HOME/.m2/repository/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:$HOME/.m2/repository/org/javassist/javassist/3.15.0-GA/javassist-3.15.0-GA.jar:$HOME/.m2/repository/org/jboss/logging/jboss-logging/3.1.0.GA/jboss-logging-3.1.0.GA.jar:$HOME/.m2/repository/org/jboss/spec/javax/transaction/jboss-transaction-api_1.1_spec/1.0.0.Final/jboss-transaction-api_1.1_spec-1.0.0.Final.jar:$HOME/.m2/repository/postgresql/postgresql/9.1-901.jdbc4/postgresql-9.1-901.jdbc4.jar:$HOME/.m2/repository/junit/junit/4.8.2/junit-4.8.2.jar:aspectjrt-1.7.2.jar org.junit.runner.JUnitCore br.edu.ufpb.threadControl.mySimpleCRM.FacadeTestJpaWithTC

done