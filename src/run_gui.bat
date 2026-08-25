@echo off
title Disaster Management System - GUI
echo ========================================================
echo Starting Disaster Management System (GUI Mode)...
echo ========================================================
javac -cp ".;lib\mysql-connector-j.jar" -d bin *.java
java -cp "bin;lib\mysql-connector-j.jar" DisasterManagement
pause
