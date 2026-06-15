@echo off
:: Arrête tous les services ESPHERE-ASS (ordre inverse)
:: Lancer en tant qu'Administrateur

net session >nul 2>&1 || (echo Droits administrateur requis. & pause & exit /b 1)

echo [ESPHERE-ASS] Arret des services...

net stop esphere-gateway-proxy  2>nul
net stop esphere-admin-api      2>nul
net stop esphere-eureka-server  2>nul
net stop esphere-config-server  2>nul

echo [OK] Tous les services sont arretes.
pause
