# ─────────────────────────────────────────────────
#  ClinicFlow — One-click build & deploy script
#  Usage: Right-click → "Run with PowerShell"
#         or: .\deploy.ps1
# ─────────────────────────────────────────────────

$ErrorActionPreference = "Stop"

$JAVA_HOME = "C:\Users\user\.jdks\ms-17.0.20.1"
$MVN       = "C:\Program Files\JetBrains\IntelliJ IDEA 2026.2\plugins\maven-plugin\lib\maven3\bin\mvn.cmd"
$CONTAINER = "clinicflow-tomcat"
$WAR       = "target\clinicflow.war"
$APP_URL   = "http://localhost:8080/login"

Write-Host ""
Write-Host "  ✚ ClinicFlow Deploy Script" -ForegroundColor Cyan
Write-Host "  ─────────────────────────────" -ForegroundColor DarkGray

# Step 1: Build
Write-Host ""
Write-Host "  [1/3] Building WAR..." -ForegroundColor Yellow
$env:JAVA_HOME = $JAVA_HOME
& $MVN clean package -DskipTests -q
Write-Host "        Build complete ✓" -ForegroundColor Green

# Step 2: Deploy to Docker
Write-Host ""
Write-Host "  [2/3] Deploying to Docker ($CONTAINER)..." -ForegroundColor Yellow
docker cp $WAR "${CONTAINER}:/usr/local/tomcat/webapps/ROOT.war"
docker cp $WAR "${CONTAINER}:/usr/local/tomcat/webapps/clinicflow.war"
Write-Host "        WAR copied ✓" -ForegroundColor Green

# Step 3: Done
Write-Host ""
Write-Host "  [3/3] Tomcat is redeploying (~20-30 sec)..." -ForegroundColor Yellow
Write-Host ""
Write-Host "  ─────────────────────────────" -ForegroundColor DarkGray
Write-Host "  Open: $APP_URL" -ForegroundColor Cyan
Write-Host "  Then press Ctrl+Shift+R to hard refresh." -ForegroundColor DarkGray
Write-Host ""
