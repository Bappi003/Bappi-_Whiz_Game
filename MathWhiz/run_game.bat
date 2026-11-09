@echo off
title BAPPI - MATH WHIZ Launcher
echo =======================================================================
echo          BAPPI - MATH WHIZ (Java Servlet and JDBC Game)
echo =======================================================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-21.0.11
set TOMCAT_HOME=C:\Users\user\Downloads\apache-tomcat-10.1.57-windows-x64\apache-tomcat-10.1.57

:: 1. Check if MySQL is running on port 3307 or 3306
netstat -ano | findstr ":3307 :3306" | findstr "LISTENING" >nul
if %errorlevel% neq 0 (
    echo [*] Starting MySQL Server...
    start /b "" "C:\xampp\mysql\bin\mysqld.exe" --defaults-file="C:\xampp\mysql\bin\my.ini" --port=3307 --datadir="C:\xampp\mysql\data"
    timeout /t 2 /nobreak >nul
) else (
    echo [v] MySQL Server is already running.
)

:: 2. Check if Tomcat is running on port 8080
netstat -ano | findstr ":8080" | findstr "LISTENING" >nul
if %errorlevel% neq 0 (
    echo [*] Starting Apache Tomcat 10.1...
    cd /d "%TOMCAT_HOME%\bin"
    start "Apache Tomcat 10.1" catalina.bat run
    echo [*] Waiting for server to initialize...
    timeout /t 4 /nobreak >nul
) else (
    echo [v] Apache Tomcat is already running on port 8080.
)

:: 3. Open Browser to the Game
echo.
echo [*] Opening BAPPI - MATH WHIZ in browser...
start http://localhost:8080/MathWhiz/

echo.
echo =======================================================================
echo  Game URL: http://localhost:8080/MathWhiz/
echo =======================================================================
