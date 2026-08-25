@echo off
title Disaster Management System - GUI
echo ========================================================
echo Starting Disaster Management System (GUI Mode)...
echo ========================================================
cd /d "%~dp0\.."
if not exist target\classes mkdir target\classes
dir /s /b src\main\java\*.java > target\sources.txt
javac -cp ".;lib\mysql-connector-j-9.4.0.jar" -d target\classes @target\sources.txt
if errorlevel 1 pause & exit /b 1
java -cp "target\classes;lib\mysql-connector-j-9.4.0.jar" DisasterManagement
pause
