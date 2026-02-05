@echo off
cd /d C:\Users\suraw\Documents\GitHub\OOP-mini-project
echo Compiling...
javac -cp lib\sqlite-jdbc-3.44.0.0.jar;lib\slf4j-api-1.7.36.jar;lib\slf4j-simple-1.7.36.jar -d bin src\*.java
echo Running...
java -cp "bin;lib\sqlite-jdbc-3.44.0.0.jar;lib\slf4j-api-1.7.36.jar;lib\slf4j-simple-1.7.36.jar" MainDashboard
pause
