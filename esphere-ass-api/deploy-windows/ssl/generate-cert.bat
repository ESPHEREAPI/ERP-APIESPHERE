@echo off
:: ==============================================================================
:: ESPHERE-ASS — Génération des certificats SSL pour Windows Server
::
:: Génère :
::   1. server.crt + server.key  → Apache (WampServer) — port 443
::   2. keystore.p12             → Gateway Spring Boot  — port 4141
::
:: Utilise OpenSSL fourni avec WampServer.
:: A exécuter DIRECTEMENT sur le serveur Windows.
::
:: Résultat :
::   deploy-windows\ssl\server.crt
::   deploy-windows\ssl\server.key
::   deploy-windows\ssl\keystore.p12
::   (puis deploy.bat copie tout vers C:\esphere-ass\certs\)
:: ==============================================================================

setlocal enabledelayedexpansion

:: ── Chemin WampServer (adapter si nécessaire) ─────────────────────────────────
set "OPENSSL=C:\wamp64v3_7\bin\apache\apache2.4.62.1\bin\openssl.exe"
set "SSL_APACHE_DIR=C:\wamp64v3_7\bin\apache\apache2.4.62.1\conf\ssl"

:: Chercher OpenSSL si le chemin ci-dessus ne correspond pas
if not exist "%OPENSSL%" (
    for /d %%v in (C:\wamp64v3_7\bin\apache\*) do (
        if exist "%%v\bin\openssl.exe" set "OPENSSL=%%v\bin\openssl.exe"
    )
)
if not exist "%OPENSSL%" (
    for /d %%v in (C:\wamp64\bin\apache\*) do (
        if exist "%%v\bin\openssl.exe" set "OPENSSL=%%v\bin\openssl.exe"
    )
)
if not exist "%OPENSSL%" (
    echo ERREUR : OpenSSL introuvable dans WampServer.
    echo         Verifiez C:\wamp64\bin\apache\apache2.x.x\bin\openssl.exe
    echo         ou installez OpenSSL Win64 depuis https://slproweb.com/products/Win32OpenSSL.html
    pause & exit /b 1
)
echo [OK] OpenSSL : %OPENSSL%

:: ── Paramètres ────────────────────────────────────────────────────────────────
set "OUT=%~dp0"
set "SERVER_IP=77.68.94.193"
set "DAYS=730"
set "KEYSTORE_PASSWORD=change_me_keystore_password"
set "KEY_ALIAS=gateway"

:: Lire le mot de passe depuis application.properties si disponible
if exist "%~dp0..\env\application.properties" (
    for /f "tokens=1,* delims==" %%a in ("%~dp0..\env\application.properties") do (
        if "%%a"=="KEYSTORE_PASSWORD" set "KEYSTORE_PASSWORD=%%b"
        if "%%a"=="KEY_ALIAS"         set "KEY_ALIAS=%%b"
        if "%%a"=="SERVER_IP"         set "SERVER_IP=%%b"
    )
)

echo.
echo ── Parametres ─────────────────────────────────────────────
echo    IP serveur : %SERVER_IP%
echo    Validite   : %DAYS% jours
echo    Alias      : %KEY_ALIAS%
echo    Sortie     : %OUT%
echo ───────────────────────────────────────────────────────────
echo.

:: ── Fichier de config OpenSSL (SAN) ──────────────────────────────────────────
set "OPENSSL_CNF=%OUT%openssl-san.cnf"
(
echo [req]
echo default_bits        = 2048
echo distinguished_name  = dn
echo req_extensions      = v3_req
echo x509_extensions     = v3_ca
echo prompt              = no
echo.
echo [dn]
echo C  = FR
echo ST = IDF
echo L  = Paris
echo O  = Zenithe
echo OU = ESPHERE
echo CN = %SERVER_IP%
echo.
echo [v3_req]
echo subjectAltName = @alt_names
echo.
echo [v3_ca]
echo subjectAltName      = @alt_names
echo basicConstraints    = CA:FALSE
echo keyUsage            = digitalSignature, keyEncipherment
echo extendedKeyUsage    = serverAuth
echo.
echo [alt_names]
echo IP.1 = %SERVER_IP%
echo DNS.1 = localhost
echo IP.2  = 127.0.0.1
) > "%OPENSSL_CNF%"

:: ── 1. Génération clé privée + certificat auto-signé (PEM) ───────────────────
echo [1/2] Generation certificat Apache (PEM)...
"%OPENSSL%" req -x509 -newkey rsa:2048 -sha256 ^
    -keyout "%OUT%server.key" ^
    -out    "%OUT%server.crt" ^
    -days   %DAYS% ^
    -nodes ^
    -config "%OPENSSL_CNF%"
if %errorlevel% neq 0 (echo ERREUR generation PEM & pause & exit /b 1)
echo [OK] server.crt + server.key generes.

:: ── 2. Génération keystore PKCS12 (Gateway Spring Boot) ──────────────────────
echo [2/2] Generation keystore PKCS12 (Gateway)...
"%OPENSSL%" pkcs12 -export ^
    -in  "%OUT%server.crt" ^
    -inkey "%OUT%server.key" ^
    -out "%OUT%keystore.p12" ^
    -name "%KEY_ALIAS%" ^
    -passout "pass:%KEYSTORE_PASSWORD%"
if %errorlevel% neq 0 (echo ERREUR generation PKCS12 & pause & exit /b 1)
echo [OK] keystore.p12 genere.

:: Copie automatique vers le dossier SSL d'Apache (WampServer)
if not exist "%SSL_APACHE_DIR%" mkdir "%SSL_APACHE_DIR%"
copy /y "%OUT%server.crt" "%SSL_APACHE_DIR%\gateway.crt" >nul
copy /y "%OUT%server.key" "%SSL_APACHE_DIR%\gateway.key" >nul
echo [OK] Certificats copies vers %SSL_APACHE_DIR%

:: Copie du keystore vers C:\esphere-ass\certs\
if not exist "C:\esphere-ass\certs" mkdir "C:\esphere-ass\certs"
copy /y "%OUT%keystore.p12" "C:\esphere-ass\certs\keystore.p12" >nul
echo [OK] keystore.p12 copie vers C:\esphere-ass\certs\

:: Nettoyage
del /q "%OPENSSL_CNF%" >nul 2>&1

echo.
echo ============================================================
echo  CERTIFICATS GENERES ET INSTALLES
echo ============================================================
echo  Apache (WampServer) :
echo    %SSL_APACHE_DIR%\gateway.crt
echo    %SSL_APACHE_DIR%\gateway.key
echo  Gateway Spring Boot :
echo    C:\esphere-ass\certs\keystore.p12
echo.
echo  ATTENTION : Certificat auto-signe — le navigateur affichera
echo              un avertissement. Pour la production, utilisez
echo              un certificat d'une CA officielle (Let's Encrypt).
echo ============================================================
echo.
endlocal
pause
