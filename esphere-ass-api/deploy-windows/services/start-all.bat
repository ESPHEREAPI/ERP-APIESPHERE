@echo off
:: Démarre tous les services ESPHERE-ASS dans l'ordre correct
:: Lancer en tant qu'Administrateur

net session >nul 2>&1 || (echo Droits administrateur requis. & pause & exit /b 1)

echo [ESPHERE-ASS] Demarrage des services...

echo [1/4] Config Server...
net start esphere-config-server
timeout /t 15 /nobreak >nul

echo [2/4] Eureka Server...
net start esphere-eureka-server
timeout /t 15 /nobreak >nul

echo [3/4] Administration API...
net start esphere-admin-api
timeout /t 20 /nobreak >nul

echo [4/4] Gateway Proxy...
net start esphere-gateway-proxy
timeout /t 10 /nobreak >nul

echo.
echo [ESPHERE-ASS] Verification du statut :
sc query esphere-config-server  | findstr "STATE"
sc query esphere-eureka-server  | findstr "STATE"
sc query esphere-admin-api      | findstr "STATE"
sc query esphere-gateway-proxy  | findstr "STATE"
echo.
echo Logs : C:\esphere-ass\logs\
pause
