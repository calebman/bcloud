@echo off
:: Set jar name
set JAR_NAME=bcloud-server
:: Select the environment variables in which the package will run
set /p ACTION_MODE_INPUT=Select the environment variables in which the package will run, default env [test]:
if not "%ACTION_MODE_INPUT%" equ "" (set ACTION_MODE=%ACTION_MODE_INPUT%) else (set ACTION_MODE=test)
echo STARTING APPLICATION ACTION_MODE:%ACTION_MODE%
echo JAR_NAME:[%JAR_NAME%]
set PROCESS_NAME=JAVA_APP_%JAR_NAME%_%ACTION_MODE%
echo PROCESS_NAME:[%PROCESS_NAME%]
tasklist /nh /fi "WINDOWTITLE eq %PROCESS_NAME%"|find /i "cmd.exe" >nul
if ERRORLEVEL 1 (echo Application already stop) else (taskkill /fi "WINDOWTITLE eq %PROCESS_NAME%" >nul & echo Application stop success)
echo This window will close in 10 seconds
ping 127.1 -n 11 >nul