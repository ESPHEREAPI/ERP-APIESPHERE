#Requires -Version 4.0
<#
.SYNOPSIS
    ESPHERE-ASS - Script de lancement et monitoring des microservices
#>

$ErrorActionPreference = "Continue"

# ==============================================================================
#  CONFIGURATION
# ==============================================================================
$BASE_DIR  = "C:\zen-it-biometrie"
$LOGS_DIR  = "$BASE_DIR\logs"
$PIDS_FILE = "$LOGS_DIR\.pids.json"

$SERVICES = @(
    [ordered]@{ Name="Eureka Server";      Key="eureka";  Dir=$BASE_DIR; Jar="zenithe-eureka-server.jar";         Port=8762; Color="Cyan";    StartOrder=1; WaitSecs=20 },
    [ordered]@{ Name="Config Server";      Key="config";  Dir=$BASE_DIR; Jar="zenithe-config-serveur.jar";        Port=8888; Color="Magenta"; StartOrder=2; WaitSecs=10 },
    [ordered]@{ Name="Gateway Proxy";      Key="gateway"; Dir=$BASE_DIR; Jar="zenithe-gateway-proxy.jar";         Port=4141; Color="Yellow";  StartOrder=3; WaitSecs=12 },
    [ordered]@{ Name="Administration API"; Key="admin";   Dir=$BASE_DIR; Jar="esphere-ass-administration.jar";    Port=8083; Color="Green";   StartOrder=4; WaitSecs=10 }
)

$JAVA_OPTS = "--spring.cloud.config.enabled=false --spring.profiles.active=prod"

# ==============================================================================
#  HELPERS CONSOLE
# ==============================================================================
function Write-Header {
    Clear-Host
    Write-Host ""
    Write-Host "  ============================================================" -ForegroundColor DarkCyan
    Write-Host "     ESPHERE-ASS  |  Microservices Manager" -ForegroundColor DarkCyan
    Write-Host "     Spring Boot 3.4.4  |  Java 21  |  Oracle DB" -ForegroundColor DarkCyan
    Write-Host "  ============================================================" -ForegroundColor DarkCyan
    Write-Host ""
}

function Write-Line {
    Write-Host "  ------------------------------------------------------------" -ForegroundColor DarkGray
}

function Write-Menu {
    Write-Header
    Write-Host "  MENU PRINCIPAL" -ForegroundColor White
    Write-Line
    Write-Host "   [1]  Demarrer TOUS les services"    -ForegroundColor Green
    Write-Host "   [2]  Demarrer un service specifique"
    Write-Host "   [3]  Arreter TOUS les services"     -ForegroundColor Red
    Write-Host "   [4]  Arreter un service specifique"
    Write-Host "   [5]  Tableau de bord (etat + ports)" -ForegroundColor Cyan
    Write-Host "   [6]  Logs GATEWAY"                  -ForegroundColor Yellow
    Write-Host "   [7]  Logs ADMINISTRATION API"       -ForegroundColor Green
    Write-Host "   [8]  Logs EUREKA"                   -ForegroundColor Cyan
    Write-Host "   [9]  Logs CONFIG SERVER"            -ForegroundColor Magenta
    Write-Host "   [0]  Quitter"
    Write-Line
    Write-Host ""
}

function Pause-Prompt {
    Write-Host ""
    Write-Host "  Appuyez sur Entree pour revenir au menu..." -ForegroundColor DarkGray
    $null = Read-Host
}

# ==============================================================================
#  GESTION DES PIDS
# ==============================================================================
function Load-Pids {
    if (Test-Path $PIDS_FILE) {
        try {
            $raw = Get-Content $PIDS_FILE -Raw -ErrorAction SilentlyContinue
            if ($raw) { return $raw | ConvertFrom-Json }
        } catch {}
    }
    return New-Object PSObject
}

function Save-Pids($pids) {
    try { $pids | ConvertTo-Json | Out-File $PIDS_FILE -Encoding utf8 -ErrorAction SilentlyContinue } catch {}
}

function Get-PidForService($key) {
    try {
        $pids = Load-Pids
        $prop = $pids.PSObject.Properties | Where-Object { $_.Name -eq $key }
        if ($prop) { return [int]$prop.Value }
    } catch {}
    return $null
}

function Set-PidForService($key, $pidVal) {
    try {
        $pids = Load-Pids
        $pids | Add-Member -NotePropertyName $key -NotePropertyValue $pidVal -Force
        Save-Pids $pids
    } catch {}
}

function Clear-PidForService($key) {
    try {
        $pids = Load-Pids
        $prop = $pids.PSObject.Properties | Where-Object { $_.Name -eq $key }
        if ($prop) { $pids.PSObject.Properties.Remove($key) }
        Save-Pids $pids
    } catch {}
}

# ==============================================================================
#  ETAT D'UN SERVICE
# ==============================================================================
function Get-ServiceStatus($svc) {
    $portOpen = $false
    try {
        $tcp  = New-Object System.Net.Sockets.TcpClient
        $conn = $tcp.BeginConnect("127.0.0.1", $svc.Port, $null, $null)
        $ok   = $conn.AsyncWaitHandle.WaitOne(500, $false)
        if ($ok -and $tcp.Connected) { $portOpen = $true }
        $tcp.Close()
    } catch {}

    if ($portOpen) { return "RUNNING" }

    $pidVal = Get-PidForService $svc.Key
    if ($null -ne $pidVal) {
        try { $null = Get-Process -Id $pidVal -ErrorAction Stop; return "STARTING" } catch {}
    }
    return "STOPPED"
}

# ==============================================================================
#  TABLEAU DE BORD
# ==============================================================================
function Show-Dashboard {
    Write-Header
    Write-Host ("  ETAT DES SERVICES  " + (Get-Date -Format "HH:mm:ss")) -ForegroundColor White
    Write-Line
    Write-Host ("  {0,-22} {1,-8} {2,-12} {3}" -f "SERVICE", "PORT", "ETAT", "PID") -ForegroundColor DarkGray
    Write-Line

    foreach ($svc in $SERVICES) {
        $status = Get-ServiceStatus $svc
        $pidVal = Get-PidForService $svc.Key
        $pidStr = if ($null -ne $pidVal) { "$pidVal" } else { "-" }

        $marker = switch ($status) {
            "RUNNING"  { "[ON] " }
            "STARTING" { "[..] " }
            default    { "[--] " }
        }
        $col = switch ($status) {
            "RUNNING"  { "Green" }
            "STARTING" { "Yellow" }
            default    { "DarkGray" }
        }

        $line = ("  {0}{1,-21} :{2,-7} {3,-12} {4}" -f $marker, $svc.Name, $svc.Port, $status, $pidStr)
        Write-Host $line -ForegroundColor $col
    }

    Write-Line
    Write-Host ""
    Write-Host "  Liens :" -ForegroundColor DarkGray
    Write-Host "   Eureka     -> http://localhost:8762"              -ForegroundColor DarkGray
    Write-Host "   Config     -> http://localhost:8888/actuator"     -ForegroundColor DarkGray
    Write-Host "   Gateway    -> https://localhost:4141/actuator/health" -ForegroundColor DarkGray
    Write-Host "   Admin API  -> http://localhost:8083/actuator/health"  -ForegroundColor DarkGray
    Write-Host ""
    Pause-Prompt
}

# ==============================================================================
#  DEMARRAGE
# ==============================================================================
function Start-Svc($svc) {
    $jar = "$($svc.Dir)\$($svc.Jar)"

    if (!(Test-Path $jar)) {
        Write-Host "  [ERREUR] JAR introuvable : $jar" -ForegroundColor Red
        return
    }

    # Verifier que java est accessible
    $javaPath = (Get-Command java -ErrorAction SilentlyContinue)
    if (!$javaPath) {
        Write-Host "  [ERREUR] 'java' introuvable dans le PATH." -ForegroundColor Red
        Write-Host "  Verifiez que JDK 21 est installe et JAVA_HOME configure." -ForegroundColor Yellow
        Pause-Prompt
        return
    }

    $status = Get-ServiceStatus $svc
    if ($status -eq "RUNNING") {
        Write-Host ("  [{0}] deja actif sur le port {1}" -f $svc.Name, $svc.Port) -ForegroundColor Yellow
        return
    }

    if (!(Test-Path $LOGS_DIR)) { New-Item -ItemType Directory -Path $LOGS_DIR -Force | Out-Null }

    $logFile = "$LOGS_DIR\$($svc.Key).log"
    # Horodatage dans le log
    "=== Demarrage $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') ===" | Out-File $logFile -Encoding utf8 -Append

    $cmdArgs = "/c java -jar `"$jar`" --server.port=$($svc.Port) $JAVA_OPTS >> `"$logFile`" 2>&1"

    Write-Host ("  Demarrage de [{0}] port {1}..." -f $svc.Name, $svc.Port) -ForegroundColor $svc.Color

    $proc = Start-Process -FilePath "cmd.exe" `
        -ArgumentList $cmdArgs `
        -WorkingDirectory $svc.Dir `
        -PassThru `
        -WindowStyle Hidden

    Set-PidForService $svc.Key $proc.Id

    Write-Host ("  -> PID {0} | log : logs\{1}.log" -f $proc.Id, $svc.Key) -ForegroundColor DarkGray
    Write-Host ("  -> Attente {0}s pour le demarrage..." -f $svc.WaitSecs) -ForegroundColor DarkGray
    Start-Sleep -Seconds $svc.WaitSecs

    $status2 = Get-ServiceStatus $svc
    if ($status2 -eq "RUNNING") {
        Write-Host ("  OK  {0} ACTIF (port {1})" -f $svc.Name, $svc.Port) -ForegroundColor Green
    } elseif ($status2 -eq "STARTING") {
        Write-Host ("  ..  {0} en cours de demarrage" -f $svc.Name) -ForegroundColor Yellow
    } else {
        Write-Host ("  ERR {0} n'a pas demarre - verifiez logs\{1}.log" -f $svc.Name, $svc.Key) -ForegroundColor Red
    }
}

function Start-AllServices {
    Write-Header
    Write-Host "  DEMARRAGE DE TOUS LES SERVICES" -ForegroundColor White
    Write-Line
    foreach ($svc in ($SERVICES | Sort-Object { $_.StartOrder })) {
        Start-Svc $svc
        Write-Host ""
    }
    Show-Dashboard
}

function Start-OneService {
    Write-Header
    Write-Host "  DEMARRER UN SERVICE" -ForegroundColor White
    Write-Line
    for ($i = 0; $i -lt $SERVICES.Count; $i++) {
        Write-Host ("   [{0}]  {1}  (port {2})" -f ($i + 1), $SERVICES[$i].Name, $SERVICES[$i].Port)
    }
    Write-Host "   [0]  Retour"
    Write-Line
    $choice = Read-Host "  Votre choix"
    if ($choice -match '^\d+$') {
        $idx = [int]$choice - 1
        if ($idx -ge 0 -and $idx -lt $SERVICES.Count) {
            Write-Host ""
            Start-Svc $SERVICES[$idx]
            Pause-Prompt
        }
    }
}

# ==============================================================================
#  ARRET
# ==============================================================================
function Stop-Svc($svc) {
    Write-Host ("  Arret de [{0}]..." -f $svc.Name) -ForegroundColor Yellow

    $pidVal = Get-PidForService $svc.Key
    if ($null -ne $pidVal) {
        try {
            $children = Get-CimInstance Win32_Process -ErrorAction SilentlyContinue |
                Where-Object { $_.ParentProcessId -eq $pidVal -and $_.Name -like "*java*" }
            foreach ($c in $children) {
                Stop-Process -Id $c.ProcessId -Force -ErrorAction SilentlyContinue
            }
            Stop-Process -Id $pidVal -Force -ErrorAction SilentlyContinue
        } catch {}
        Clear-PidForService $svc.Key
    }

    # Tuer tout process encore sur ce port
    try {
        $lines = netstat -ano 2>$null | Select-String (":$($svc.Port)\s")
        foreach ($line in $lines) {
            if ($line -match '\s(\d+)$') {
                $jPid = [int]$Matches[1]
                if ($jPid -gt 4) {
                    Stop-Process -Id $jPid -Force -ErrorAction SilentlyContinue
                }
            }
        }
    } catch {}

    Write-Host ("  OK  {0} arrete." -f $svc.Name) -ForegroundColor Green
}

function Stop-AllServices {
    Write-Header
    Write-Host "  ARRET DE TOUS LES SERVICES" -ForegroundColor Red
    Write-Line
    foreach ($svc in ($SERVICES | Sort-Object { -$_.StartOrder })) {
        Stop-Svc $svc
        Write-Host ""
    }
    Pause-Prompt
}

function Stop-OneService {
    Write-Header
    Write-Host "  ARRETER UN SERVICE" -ForegroundColor Red
    Write-Line
    for ($i = 0; $i -lt $SERVICES.Count; $i++) {
        $status = Get-ServiceStatus $SERVICES[$i]
        $col = if ($status -eq "RUNNING") { "Green" } elseif ($status -eq "STARTING") { "Yellow" } else { "DarkGray" }
        Write-Host ("   [{0}]  {1}  [{2}]" -f ($i + 1), $SERVICES[$i].Name, $status) -ForegroundColor $col
    }
    Write-Host "   [0]  Retour"
    Write-Line
    $choice = Read-Host "  Votre choix"
    if ($choice -match '^\d+$') {
        $idx = [int]$choice - 1
        if ($idx -ge 0 -and $idx -lt $SERVICES.Count) {
            Write-Host ""
            Stop-Svc $SERVICES[$idx]
            Pause-Prompt
        }
    }
}

# ==============================================================================
#  VIEWER DE LOGS
# ==============================================================================
function Write-ColoredLog($line, $defaultColor) {
    if ($line -match "ERROR|Exception|FATAL|SEVERE") {
        Write-Host $line -ForegroundColor Red
    } elseif ($line -match " WARN ") {
        Write-Host $line -ForegroundColor Yellow
    } elseif ($line -match "Started .* in |Tomcat started|Netty started|Started Application") {
        Write-Host $line -ForegroundColor Green
    } elseif ($line -match " INFO ") {
        Write-Host $line -ForegroundColor $defaultColor
    } else {
        Write-Host $line -ForegroundColor DarkGray
    }
}

function Show-ServiceLog($key, $title, $color) {
    $logFile = "$LOGS_DIR\$key.log"
    Clear-Host
    Write-Host ""
    Write-Host ("  === LOGS : {0} ===" -f $title) -ForegroundColor $color
    Write-Host "  Fichier : $logFile" -ForegroundColor DarkGray
    Write-Host "  [Ctrl+C puis Entree pour arreter]" -ForegroundColor DarkGray
    Write-Line

    if (!(Test-Path $logFile)) {
        Write-Host "  Aucun log trouve. Le service a-t-il ete demarre ?" -ForegroundColor Yellow
        Pause-Prompt
        return
    }

    Get-Content $logFile -Tail 60 | ForEach-Object { Write-ColoredLog $_ $color }
    Write-Host ""
    Write-Host "  -- Suivi temps reel (Ctrl+C pour arreter) --" -ForegroundColor DarkGray

    try {
        Get-Content $logFile -Wait -Tail 0 | ForEach-Object { Write-ColoredLog $_ $color }
    } catch { }

    Pause-Prompt
}

# ==============================================================================
#  INITIALISATION
# ==============================================================================
try {
    if (!(Test-Path $LOGS_DIR)) {
        New-Item -ItemType Directory -Path $LOGS_DIR -Force | Out-Null
    }
} catch {
    Write-Host "Impossible de creer le dossier logs : $_" -ForegroundColor Yellow
}

# ==============================================================================
#  BOUCLE PRINCIPALE
# ==============================================================================
while ($true) {
    try {
        Write-Menu
        $choice = Read-Host "  Votre choix"

        switch ($choice.Trim()) {
            "1" { Start-AllServices }
            "2" { Start-OneService }
            "3" { Stop-AllServices }
            "4" { Stop-OneService }
            "5" { Show-Dashboard }
            "6" { Show-ServiceLog "gateway" "GATEWAY PROXY (port 4141)"        "Yellow" }
            "7" { Show-ServiceLog "admin"   "ADMINISTRATION API (port 8083)"   "Green"  }
            "8" { Show-ServiceLog "eureka"  "EUREKA SERVER (port 8762)"        "Cyan"   }
            "9" { Show-ServiceLog "config"  "CONFIG SERVER (port 8888)"        "Magenta"}
            "0" {
                Write-Host ""
                Write-Host "  Au revoir." -ForegroundColor DarkGray
                Write-Host ""
                exit 0
            }
            default {
                Write-Host "  Choix invalide." -ForegroundColor Red
                Start-Sleep -Seconds 1
            }
        }
    } catch {
        Write-Host ""
        Write-Host ("  [ERREUR] {0}" -f $_.Exception.Message) -ForegroundColor Red
        Write-Host "  Appuyez sur Entree pour continuer..." -ForegroundColor DarkGray
        $null = Read-Host
    }
}
