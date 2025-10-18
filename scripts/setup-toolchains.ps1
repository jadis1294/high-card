param(
    [string]$JdkHome
)

if (-not $JdkHome) {
    Write-Host "No JDK path supplied. Please enter the full path to your JDK 21 installation (eg: C:\\Program Files\\Java\\jdk-21):"
    $JdkHome = Read-Host
}

if (-not (Test-Path $JdkHome)) {
    Write-Error "The path '$JdkHome' does not exist. Aborting."
    exit 1
}

$m2Dir = Join-Path $env:USERPROFILE ".m2"
if (-not (Test-Path $m2Dir)) {
    New-Item -ItemType Directory -Path $m2Dir -Force | Out-Null
}

$toolchainsPath = Join-Path $m2Dir "toolchains.xml"
if (Test-Path $toolchainsPath) {
    $bak = "$toolchainsPath.bak.$((Get-Date).ToString('yyyyMMddHHmmss'))"
    Write-Host "Existing toolchains.xml found. Backing up to: $bak"
    Copy-Item -Path $toolchainsPath -Destination $bak -Force
}

$xml = @"
<?xml version="1.0" encoding="UTF-8"?>
<toolchains xmlns="http://maven.apache.org/TOOLCHAINS/1.1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/TOOLCHAINS/1.1.0 https://maven.apache.org/xsd/toolchains-1.1.0.xsd">
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>21</version>
      <vendor>any</vendor>
    </provides>
    <configuration>
      <jdkHome>$JdkHome</jdkHome>
    </configuration>
  </toolchain>
</toolchains>
"@

Set-Content -Path $toolchainsPath -Value $xml -Encoding UTF8
Write-Host "Wrote toolchains.xml to: $toolchainsPath"
Write-Host "Run: .\mvnw.cmd -DskipTests=false clean test to build the project with the toolchain."
