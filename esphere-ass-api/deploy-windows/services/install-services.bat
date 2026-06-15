@echo off
:: ==============================================================================
:: ESPHERE-ASS — Installation des services Windows avec NSSM
::
:: Prérequis :
::   - NSSM (Non-Sucking Service Manager) : https://nssm.cc/download
::     -> Placer nssm.exe dans C:\tools\nssm\nssm.exe
::   - JDK 21 installé (Eclipse Temurin recommandé)
::   - Déploiement réalisé (deploy.bat exécuté)
::   - Fichier C:\esphere-ass\admin-api\application.properties configuré
::
:: Lancer en tant qu'Administrateur
::
:: Ordre de démarrage :
::   1. esphere-config-server  (8888)
::   2. esphere-eureka-server  (8762)
::   3. esphere-admin-api      (8083)
::   4. esphere-gateway-proxy  (4141)
:: ==============================================================================

setlocal enabledelayedexpansion

:: ── VARIABLES A ADAPTER SELON LE SERVEUR ──────────────────────────────────────
set "NSSM=C:\tools\nssm\nssm.exe"
set "JAVA=C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot\bin\java.exe"
set "DEPLOY=C:\esphere-ass"
set "LOGS=%DEPLOY%\logs"

:: Lire les secrets depuis application.properties si présent
set "ORACLE_URL="
set "ORACLE_USER="
set "ORACLE_PASSWORD="
set "JWT_SECRET="
set "GIT_CONFIG_URI="
set "GIT_CONFIG_BRANCH=main"
set "GIT_USERNAME="
set "GIT_PASSWORD="
set "KEYSTORE_PASSWORD="
set "KEY_ALIAS=gateway"

if exist "%DEPLOY%\admin-api\application.properties" (
    for /f "usebackq tokens=1,* delims==" %%a in ("%DEPLOY%\admin-api\application.properties") do (
        if "%%a"=="ORACLE_URL"        set "ORACLE_URL=%%b"
        if "%%a"=="ORACLE_USER"       set "ORACLE_USER=%%b"
        if "%%a"=="ORACLE_PASSWORD"   set "ORACLE_PASSWORD=%%b"
        if "%%a"=="JWT_SECRET"        set "JWT_SECRET=%%b"
        if "%%a"=="GIT_CONFIG_URI"    set "GIT_CONFIG_URI=%%b"
        if "%%a"=="GIT_CONFIG_BRANCH" set "GIT_CONFIG_BRANCH=%%b"
        if "%%a"=="GIT_USERNAME"      set "GIT_USERNAME=%%b"
        if "%%a"=="GIT_PASSWORD"      set "GIT_PASSWORD=%%b"
        if "%%a"=="KEYSTORE_PASSWORD" set "KEYSTORE_PASSWORD=%%b"
        if "%%a"=="KEY_ALIAS"         set "KEY_ALIAS=%%b"
    )
) else (
    echo ERREUR : %DEPLOY%\admin-api\application.properties introuvable.
    echo         Copiez deploy-windows\env\application.properties.example
    echo         vers %DEPLOY%\admin-api\application.properties et remplissez-le.
    pause & exit /b 1
)

:: Vérifications
if not exist "%NSSM%" (
    echo ERREUR : NSSM introuvable : %NSSM%
    echo         Telechargez nssm.exe depuis https://nssm.cc/download
    echo         et placez-le dans C:\tools\nssm\
    pause & exit /b 1
)
if not exist "%JAVA%" (
    echo ERREUR : Java introuvable : %JAVA%
    echo         Installez Eclipse Temurin JDK 21 depuis https://adoptium.net
    echo         et mettez a jour la variable JAVA dans ce script.
    pause & exit /b 1
)
net session >nul 2>&1 || (echo ERREUR : Droits administrateur requis. & pause & exit /b 1)

if not exist "%LOGS%" mkdir "%LOGS%"

echo.
echo [ESPHERE-ASS] Installation des services Windows...
echo.

:: ==============================================================================
:: SERVICE 1 — Config Server
:: ==============================================================================
echo [1/4] Installation esphere-config-server...
%NSSM% install esphere-config-server "%JAVA%"
%NSSM% set esphere-config-server AppParameters ^
    -jar "%DEPLOY%\config-server\config-server.jar" ^
    -Dspring.profiles.active=prod ^
    -Dserver.ssl.enabled=false ^
    -Dserver.port=8888 ^
    -DGIT_CONFIG_URI="%GIT_CONFIG_URI%" ^
    -DGIT_CONFIG_BRANCH="%GIT_CONFIG_BRANCH%" ^
    -DGIT_USERNAME="%GIT_USERNAME%" ^
    -DGIT_PASSWORD="%GIT_PASSWORD%"
%NSSM% set esphere-config-server AppDirectory "%DEPLOY%\config-server"
%NSSM% set esphere-config-server AppStdout "%LOGS%\config-server.log"
%NSSM% set esphere-config-server AppStderr "%LOGS%\config-server-error.log"
%NSSM% set esphere-config-server AppRotateFiles 1
%NSSM% set esphere-config-server AppRotateBytes 52428800
%NSSM% set esphere-config-server Start SERVICE_AUTO_START
%NSSM% set esphere-config-server Description "ESPHERE-ASS Config Server (Spring Cloud Config)"
echo [OK] esphere-config-server installe.

:: ==============================================================================
:: SERVICE 2 — Eureka Server
:: ==============================================================================
echo [2/4] Installation esphere-eureka-server...
%NSSM% install esphere-eureka-server "%JAVA%"
%NSSM% set esphere-eureka-server AppParameters ^
    -jar "%DEPLOY%\eureka-server\eureka-server.jar" ^
    -Dspring.profiles.active=prod ^
    -Dserver.ssl.enabled=false ^
    -Dserver.port=8762 ^
    -Deureka.instance.hostname=localhost ^
    -Deureka.instance.ip-address=127.0.0.1 ^
    -Deureka.instance.secure-port-enabled=false ^
    -Deureka.instance.non-secure-port-enabled=true
%NSSM% set esphere-eureka-server AppDirectory "%DEPLOY%\eureka-server"
%NSSM% set esphere-eureka-server AppStdout "%LOGS%\eureka-server.log"
%NSSM% set esphere-eureka-server AppStderr "%LOGS%\eureka-server-error.log"
%NSSM% set esphere-eureka-server AppRotateFiles 1
%NSSM% set esphere-eureka-server AppRotateBytes 52428800
%NSSM% set esphere-eureka-server Start SERVICE_AUTO_START
%NSSM% set esphere-eureka-server DependOnService esphere-config-server
%NSSM% set esphere-eureka-server Description "ESPHERE-ASS Eureka Server (Spring Cloud Netflix)"
echo [OK] esphere-eureka-server installe.

:: ==============================================================================
:: SERVICE 3 — Administration API
:: ==============================================================================
echo [3/4] Installation esphere-admin-api...
%NSSM% install esphere-admin-api "%JAVA%"
%NSSM% set esphere-admin-api AppParameters ^
    -jar "%DEPLOY%\admin-api\admin-api.jar" ^
    -Dspring.profiles.active=prod ^
    -Dserver.ssl.enabled=false ^
    -Dserver.port=8083 ^
    -DORACLE_URL="%ORACLE_URL%" ^
    -DORACLE_USER="%ORACLE_USER%" ^
    -DORACLE_PASSWORD="%ORACLE_PASSWORD%" ^
    -DJWT_SECRET="%JWT_SECRET%" ^
    -Deureka.client.service-url.defaultZone=http://localhost:8762/eureka/ ^
    -Deureka.instance.hostname=localhost ^
    -Deureka.instance.prefer-ip-address=true
%NSSM% set esphere-admin-api AppDirectory "%DEPLOY%\admin-api"
%NSSM% set esphere-admin-api AppStdout "%LOGS%\admin-api.log"
%NSSM% set esphere-admin-api AppStderr "%LOGS%\admin-api-error.log"
%NSSM% set esphere-admin-api AppRotateFiles 1
%NSSM% set esphere-admin-api AppRotateBytes 104857600
%NSSM% set esphere-admin-api Start SERVICE_AUTO_START
%NSSM% set esphere-admin-api DependOnService esphere-eureka-server
%NSSM% set esphere-admin-api Description "ESPHERE-ASS Administration API (Spring Boot)"
echo [OK] esphere-admin-api installe.

:: ==============================================================================
:: SERVICE 4 — Gateway Proxy
:: ==============================================================================
echo [4/4] Installation esphere-gateway-proxy...
%NSSM% install esphere-gateway-proxy "%JAVA%"
%NSSM% set esphere-gateway-proxy AppParameters ^
    -jar "%DEPLOY%\gateway-proxy\gateway-proxy.jar" ^
    -Dspring.profiles.active=prod ^
    -Dserver.port=4141 ^
    -Dspring.cloud.config.uri=http://localhost:8888 ^
    -DEUREKA_SERVER_URL=http://localhost:8762/eureka/ ^
    -DEUREKA_INSTANCE_HOSTNAME=localhost ^
    -DEUREKA_INSTANCE_IP=127.0.0.1 ^
    -DSERVICE_ADMIN_URL=http://localhost:8083 ^
    -DKEYSTORE_PATH="%DEPLOY%\certs\keystore.p12" ^
    -DKEYSTORE_PASSWORD="%KEYSTORE_PASSWORD%" ^
    -DKEY_ALIAS="%KEY_ALIAS%"
%NSSM% set esphere-gateway-proxy AppDirectory "%DEPLOY%\gateway-proxy"
%NSSM% set esphere-gateway-proxy AppStdout "%LOGS%\gateway-proxy.log"
%NSSM% set esphere-gateway-proxy AppStderr "%LOGS%\gateway-proxy-error.log"
%NSSM% set esphere-gateway-proxy AppRotateFiles 1
%NSSM% set esphere-gateway-proxy AppRotateBytes 52428800
%NSSM% set esphere-gateway-proxy Start SERVICE_AUTO_START
%NSSM% set esphere-gateway-proxy DependOnService esphere-admin-api
%NSSM% set esphere-gateway-proxy Description "ESPHERE-ASS Gateway Proxy (Spring Cloud Gateway — HTTPS 4141)"
echo [OK] esphere-gateway-proxy installe.

echo.
echo ============================================================
echo  SERVICES INSTALLES
echo ============================================================
echo  Demarrer tous les services : services\start-all.bat
echo  Verifier les logs          : %LOGS%\
echo  Tableau de bord services   : services.msc
echo ============================================================
echo.
endlocal
pause
