<#
  Baut die WAR-Datei mit Maven und kopiert sie in ein lokales Tomcat 10.1.
  Aufruf:  .\scripts\deploy-war-tomcat.ps1 -TomcatHome "C:\tools\apache-tomcat-10.1.36"
  (alternativ Umgebungsvariable CATALINA_HOME setzen)
#>
param(
    [string]$TomcatHome = $env:CATALINA_HOME,
    [string]$WarName = "Fallstudie_IU.war"
)

$ErrorActionPreference = "Stop"
if (-not $TomcatHome) { throw "TomcatHome fehlt: -TomcatHome angeben oder CATALINA_HOME setzen." }

$ProjectRoot = Split-Path -Parent $PSScriptRoot
$Webapps = Join-Path $TomcatHome "webapps"
$Exploded = Join-Path $Webapps ($WarName -replace "\.war$", "")

Write-Host "--- Maven-Build ---" -ForegroundColor Cyan
Push-Location $ProjectRoot
try { mvn clean package } finally { Pop-Location }

$SourceWar = Join-Path $ProjectRoot "target\$WarName"
if (-not (Test-Path $SourceWar)) { throw "WAR nicht gefunden: $SourceWar" }

if (Test-Path $Exploded -PathType Container) {
    Write-Host "Entferne altes entpacktes Verzeichnis (Tomcat sollte gestoppt sein)..." -ForegroundColor Yellow
    Remove-Item -Path $Exploded -Recurse -Force
}

Copy-Item -Path $SourceWar -Destination $Webapps -Force
Write-Host "Bereitgestellt: $Webapps\$WarName" -ForegroundColor Green
Write-Host "Tomcat starten: $TomcatHome\bin\catalina.bat run"
