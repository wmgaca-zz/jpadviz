@echo off
echo Setting Java path...
set JAVA_PATH="C:\glassfish3\jdk7\bin\java.exe"
echo Starting the client...
%JAVA_PATH% -jar app\bin\jpadsampleclient.jar
