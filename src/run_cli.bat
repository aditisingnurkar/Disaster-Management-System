@echo off
title Disaster Management System - CLI
echo ========================================================
echo Starting Disaster Management System (Console Mode)...
echo ========================================================
javac -cp ".;lib\mysql-connector-j.jar" -d bin *.java
java -cp "bin;lib\mysql-connector-j.jar" ManagementSystem
pause
