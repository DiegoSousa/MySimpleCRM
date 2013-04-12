#!/bin/sh

# /**
#  * Build script of the entire project MySimpleCRM.
#  * 
#  * @author Diego Sousa - diego.sousa@dce.ufpb.br
#  *
#  */

echo ""
echo "For that this script works, remember that must have ajc configured on the terminal of the same way that was configured the javac."
echo "Doubts? View pag 109 in http://pt.scribd.com/doc/128958630/Proyecto-POA-AspectJ"
echo ""
echo "Enter the path to the folder .M2 of mavem or open this script and change the variable "'$pathM2'" by path to the folder .M2"
echo "Ex: If the path is /home/user/.m2 then enter with /home/user without o /.m2"
echo ""

read pathM2

echo ""
echo "Start compilation"

echo ""
echo "compiling *.java (source)"
javac -d target/classes/ -sourcepath src/main/java/:threadControl_0.3_src/srcTC/ -cp :"$pathM2"/.m2/repository/javax/javaee-web-api/6.0/javaee-web-api-6.0.jar:"$pathM2"/.m2/repository/com/sun/jersey/jersey-json/1.8/jersey-json-1.8.jar:"$pathM2"/.m2/repository/org/codehaus/jettison/jettison/1.1/jettison-1.1.jar $(find src/main/ threadControl_0.3_src/srcTC/ -name *.java)

echo ""
echo "compiling *.java (tests)"
javac -d target/classes/ -sourcepath src/main/java/:threadControl_0.3_src/srcTC/ -cp :"$pathM2"/.m2/repository/junit/junit/4.8.2/junit-4.8.2.jar:"$pathM2"/.m2/repository/javax/javaee-web-api/6.0/javaee-web-api-6.0.jar $(find src/test/ -name *.java)
mv target/classes/br/edu/ufpb/threadControl/mySimpleCRM/FacadeTestJpaWithTC.class target/classes/br/edu/ufpb/threadControl/mySimpleCRM/FacadeTestJpaWithSleep.class target/test-classes/br/edu/ufpb/threadControl/mySimpleCRM/

echo ""
echo "compiling ThreadControlAspect.aj"
ajc -d target/classes/ -source 1.6 -cp :"$pathM2"/.m2/repository/org/aspectj/aspectjrt/1.7.2/aspectjrt-1.7.2.jar:target/classes/ threadControl_0.3_src/srcAspectsTC/br/edu/ufcg/threadcontrol/aspects/ThreadControlAspect.aj 2> /dev/null

echo ""
echo "Finished compilation."
