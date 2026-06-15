@echo off
:: Désinstalle tous les services ESPHERE-ASS
:: Lancer en tant qu'Administrateur

set "NSSM=C:\tools\nssm\nssm.exe"

net session >nul 2>&1 || (echo Droits administrateur requis. & pause & exit /b 1)

echo [ESPHERE-ASS] Desinstallation des services...
echo ATTENTION : Cette action supprime les services Windows.
echo             Les JARs et logs ne sont PAS supprimes.
set /p CONFIRM=Confirmer ? (O/N) :
if /i not "%CONFIRM%"=="O" (echo Annule. & pause & exit /b 0)

net stop esphere-gateway-proxy  2>nul
net stop esphere-admin-api      2>nul
net stop esphere-eureka-server  2>nul
net stop esphere-config-server  2>nul

%NSSM% remove esphere-gateway-proxy  confirm
%NSSM% remove esphere-admin-api      confirm
%NSSM% remove esphere-eureka-server  confirm
%NSSM% remove esphere-config-server  confirm

echo [OK] Services desinstalles.
pause
