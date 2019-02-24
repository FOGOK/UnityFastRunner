:: Repack all files from Defs (without meta)

@echo off
call "%~dp0\setvars.bat"

set directorySource="%UNITYROOTPATH%\Assets\Resources\Defs"
set directoryWithResourcesToPack="%UNITYROOTPATH%\builds\build_Data\Unity_Assets_Files\resources"
set assetToPack="%UNITYROOTPATH%\builds\build_Data\resources.assets"

MD %directoryWithResourcesToPack%
rmdir %directoryWithResourcesToPack% /s /q
ROBOCOPY %directorySource% %directoryWithResourcesToPack% /XF file "*.meta"
cd %directoryWithResourcesToPack%
ren *.json *.txt
%UNITYEXPATH% import %assetToPack% -ndc