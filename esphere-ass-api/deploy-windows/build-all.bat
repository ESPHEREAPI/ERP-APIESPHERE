@echo off
:: ==============================================================================
:: ESPHERE-ASS — Build complet (Phase 1 : Windows Server 2016 + WampServer)
::
:: Prérequis sur le poste de build :
::   - JDK 21     : https://adoptium.net
::   - Maven 3.9+ : https://maven.apache.org  (ou utilise le mvnw inclus)
::   - Node.js 20 : https://nodejs.org
::   - npm 10+    : inclus avec Node
::
:: Usage :
::   build-all.bat
::
:: Résultat :
::   deploy-windows\dist\  — tous les JARs + le build Angular
:: ==============================================================================

setlocal enabledelayedexpansion

set "ROOT=%~dp0.."
set "DIST=%~dp0dist"
set "JAVA_HOME_HINT=C:\Program Files\Eclipse Adoptium\jdk-21"

:: Vérification des outils
echo.
echo [ESPHERE-ASS] Verification des outils...

where java >nul 2>&1 || (
    echo ERREUR : Java introuvable. Installez JDK 21 depuis https://adoptium.net
    echo         puis ajoutez-le au PATH systeme.
    pause & exit /b 1
)

where mvn >nul 2>&1
if %errorlevel% neq 0 (
    where mvnw >nul 2>&1
    if %errorlevel% neq 0 (
        echo ERREUR : Maven introuvable. Installez Maven 3.9+ ou utilisez le Maven Wrapper inclus.
        pause & exit /b 1
    )
    set "MVN=mvnw.cmd"
) else (
    set "MVN=mvn"
)

where node >nul 2>&1 || (
    echo ERREUR : Node.js introuvable. Installez Node 20 depuis https://nodejs.org
    pause & exit /b 1
)

java -version 2>&1 | findstr "21\." >nul
if %errorlevel% neq 0 (
    echo ATTENTION : La version Java detectee n'est peut-etre pas JDK 21.
    echo             Verifiez que JAVA_HOME pointe vers JDK 21.
)

echo [OK] Outils verifies.

:: Nettoyage et creation du dossier dist
echo.
echo [ESPHERE-ASS] Preparation du dossier dist...
if exist "%DIST%" rmdir /s /q "%DIST%"
mkdir "%DIST%\jars"
mkdir "%DIST%\web"

:: ==============================================================================
:: BUILD 1 — Config Server
:: ==============================================================================
echo.
echo [1/4] Build esphere-ass-config-serveur...
pushd "%ROOT%\esphere-ass-config-serveur"
call %MVN% clean package -DskipTests -q
if %errorlevel% neq 0 (echo ERREUR build config-server & popd & pause & exit /b 1)
copy /y "target\esphere-ass-config-serveur-0.0.1-SNAPSHOT.jar" "%DIST%\jars\config-server.jar" >nul
echo [OK] config-server.jar
popd

:: ==============================================================================
:: BUILD 2 — Eureka Server
:: ==============================================================================
echo.
echo [2/4] Build esphere-ass-eureka-server...
pushd "%ROOT%\esphere-ass-eureka-server"
call %MVN% clean package -DskipTests -q
if %errorlevel% neq 0 (echo ERREUR build eureka-server & popd & pause & exit /b 1)
copy /y "target\esphere-ass-eureka-server-0.0.1-SNAPSHOT.jar" "%DIST%\jars\eureka-server.jar" >nul
echo [OK] eureka-server.jar
popd

:: ==============================================================================
:: BUILD 3 — Administration API
:: ==============================================================================
echo.
echo [3/4] Build esphere-ass-administration-api...
pushd "%ROOT%\esphere-ass-administration-api"
call %MVN% clean package -DskipTests -q
if %errorlevel% neq 0 (echo ERREUR build admin-api & popd & pause & exit /b 1)
copy /y "target\esphere-ass-administration-api-0.0.1-SNAPSHOT.jar" "%DIST%\jars\admin-api.jar" >nul
echo [OK] admin-api.jar
popd

:: ==============================================================================
:: BUILD 4 — Gateway Proxy
:: ==============================================================================
echo.
echo [4/4] Build esphere-ass-gateway-proxy...
pushd "%ROOT%\esphere-ass-gateway-proxy"
call %MVN% clean package -DskipTests -q
if %errorlevel% neq 0 (echo ERREUR build gateway-proxy & popd & pause & exit /b 1)
copy /y "target\esphere-ass-gateway-proxy-0.0.1-SNAPSHOT.jar" "%DIST%\jars\gateway-proxy.jar" >nul
echo [OK] gateway-proxy.jar
popd

:: ==============================================================================
:: BUILD 5 — Frontend Angular
:: ==============================================================================
echo.
echo [5/5] Build Angular (production)...
pushd "%ROOT%\esphere-ass-web"
call npm ci --legacy-peer-deps --silent
if %errorlevel% neq 0 (echo ERREUR npm install & popd & pause & exit /b 1)
call npm run build -- --configuration=production
if %errorlevel% neq 0 (echo ERREUR ng build & popd & pause & exit /b 1)
xcopy /s /q /y "dist\adminlte-angular-app\browser\*" "%DIST%\web\" >nul
echo [OK] Angular build -> dist\web\
popd

:: ==============================================================================
echo.
echo ============================================================
echo  BUILD TERMINE
echo ============================================================
echo  JARs     : %DIST%\jars\
echo  Frontend : %DIST%\web\
echo.
echo  Prochaine etape : deploy-windows\deploy.bat
echo ============================================================
echo.
endlocal
pause
