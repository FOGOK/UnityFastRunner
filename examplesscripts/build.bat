@echo off
call "%~dp0\setvars.bat"

set BUILDSDIR=%UNITYROOTPATH%\builds
set LOGFILE=%UNITYROOTPATH%\stdout.log

rmdir "%BUILDSDIR%" /s /q
echo Removed or skipped %BUILDSDIR% dir
del "%LOGFILE%"
echo Removed or skipped %LOGFILE% file
"%UNITYEXEPATH%" -projectPath "%UNITYROOTPATH%" -logFile "%LOGFILE%" -quit -batchmode -executeMethod AutoBuilder.StandaloneBuild