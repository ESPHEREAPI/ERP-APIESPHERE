@echo off
:: ==============================================================================
:: ESPHERE-ASS — Déploiement vers C:\esphere-ass\  (Windows Server 2016)
::
:: A lancer APRES build-all.bat
:: Doit etre execute en tant qu'Administrateur
::
:: Structure deployee :
::   C:\esphere-ass\
::     config-server\  config-server.jar
::     eureka-server\  eureka-server.jar
::     admin-api\      admin-api.jar
::     gateway-proxy\  gateway-proxy.jar + keystore.p12 + application.properties
::     web\            Angular dist (servi par Apache/WampServer)
::     logs\
::     certs\          certificats SSL
:: ==============================================================================

setlocal enabledelayedexpansion

:: ── Configurer ici les chemins de déploiement ────────────────────────────────
set "DEPLOY_DIR=C:\esphere-ass"
set "WAMP_WEB=C:\wamp64v3_7\www\web-app\esphere-ass"
set "DIST=%~dp0dist"

:: Vérifier que le build existe
if not exist "%DIST%\jars\config-server.jar" (
    echo ERREUR : Aucun build trouve dans %DIST%\
    echo         Lancez d'abord  deploy-windows\build-all.bat
    pause & exit /b 1
)

:: Vérifier les droits admin
net session >nul 2>&1 || (
    echo ERREUR : Ce script doit etre execute en tant qu'Administrateur.
    pause & exit /b 1
)

echo.
echo [ESPHERE-ASS] Deploiement vers %DEPLOY_DIR% ...
echo.

:: Création des dossiers
for %%d in (config-server eureka-server admin-api gateway-proxy web logs certs) do (
    if not exist "%DEPLOY_DIR%\%%d" mkdir "%DEPLOY_DIR%\%%d"
)

:: Copie des JARs
echo [1/4] Copie des JARs...
copy /y "%DIST%\jars\config-server.jar"  "%DEPLOY_DIR%\config-server\config-server.jar"  >nul
copy /y "%DIST%\jars\eureka-server.jar"  "%DEPLOY_DIR%\eureka-server\eureka-server.jar"  >nul
copy /y "%DIST%\jars\admin-api.jar"      "%DEPLOY_DIR%\admin-api\admin-api.jar"          >nul
copy /y "%DIST%\jars\gateway-proxy.jar"  "%DEPLOY_DIR%\gateway-proxy\gateway-proxy.jar"  >nul
echo [OK] JARs copies.

:: Copie du frontend Angular vers WampServer
echo [2/4] Copie du frontend Angular...
if not exist "%WAMP_WEB%" mkdir "%WAMP_WEB%"
xcopy /s /e /q /y "%DIST%\web\*" "%WAMP_WEB%\" >nul
echo [OK] Frontend Angular copie vers %WAMP_WEB%

:: Copie des fichiers de configuration
echo [3/4] Copie des fichiers de configuration...
if exist "%~dp0env\application-prod.properties" (
    copy /y "%~dp0env\application-prod.properties" "%DEPLOY_DIR%\admin-api\" >nul
)
echo [OK] Configuration copiee.

:: Copie des certificats SSL (s'ils existent déjà)
echo [4/4] Verification des certificats SSL...
if exist "%~dp0ssl\keystore.p12" (
    copy /y "%~dp0ssl\keystore.p12" "%DEPLOY_DIR%\certs\keystore.p12" >nul
    echo [OK] keystore.p12 copie vers %DEPLOY_DIR%\certs\
) else (
    echo [ATTENTION] Aucun keystore.p12 trouve dans deploy-windows\ssl\
    echo             Lancez deploy-windows\ssl\generate-cert.bat sur le serveur.
)
if exist "%~dp0ssl\server.crt" (
    copy /y "%~dp0ssl\server.crt" "%DEPLOY_DIR%\certs\server.crt" >nul
    copy /y "%~dp0ssl\server.key" "%DEPLOY_DIR%\certs\server.key" >nul
    echo [OK] Certificats Apache copies.
)

echo.
echo ============================================================
echo  DEPLOIEMENT TERMINE
echo ============================================================
echo  Dossier    : %DEPLOY_DIR%
echo.
echo  Prochaines etapes :
echo   1. Configurer %DEPLOY_DIR%\admin-api\application.properties
echo      (ORACLE_URL, JWT_SECRET, etc.)
echo   2. Generer les certs SSL : ssl\generate-cert.bat
echo   3. Installer les services : services\install-services.bat
echo   4. Configurer Apache      : apache\esphere-ass.conf
echo ============================================================
echo.
endlocal
pause
