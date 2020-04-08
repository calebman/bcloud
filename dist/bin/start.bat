@echo off
:: Set jar name
set JAR_NAME=bcloud-server
set BIN_PATH=%~dp0
echo BIN_PATH:[%BIN_PATH%]
cd %BIN_PATH%
cd ..
set CONTEXT_PATH=%cd%
echo CONTEXT_PATH:[%CONTEXT_PATH%]
echo JAR_NAME:[%JAR_NAME%]
:: Select the environment variables in which the package will run
set /p ACTION_MODE_INPUT=Select the environment variables in which the package will run, default env [test]:
if not "%ACTION_MODE_INPUT%" equ "" (set ACTION_MODE=%ACTION_MODE_INPUT%) else (set ACTION_MODE=test)
echo STARTING APPLICATION ACTION_MODE:%ACTION_MODE%
set PROCESS_NAME=JAVA_APP_%JAR_NAME%_%ACTION_MODE%
title %PROCESS_NAME%
echo PROCESS_NAME:[%PROCESS_NAME%]
:: Run jar command
java -jar %JAR_NAME%.jar --spring.profiles.active=%ACTION_MODE%
pause