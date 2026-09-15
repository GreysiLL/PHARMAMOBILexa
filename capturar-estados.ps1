$ErrorActionPreference = 'Stop'
Set-Location -LiteralPath $PSScriptRoot
$jbr = Join-Path $env:ProgramFiles 'Android/Android Studio/jbr'
if (Test-Path -LiteralPath (Join-Path $jbr 'bin/java.exe')) { $env:JAVA_HOME = $jbr }
$fuente = 'shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/data/repository/LibroRepositorioEnMemoria.kt'
& git -c "safe.directory=$PSScriptRoot" diff --exit-code HEAD -- $fuente
if ($LASTEXITCODE -ne 0) { throw 'El repositorio no coincide con la versión normal aprobada. No se instalará.' }
$imagenes = @('inicio-claro.png','inicio-oscuro.png','libros-cargando.png','libros-vacio.png','libros-listado.png','libros-error.png','lectores-sin-telefono.png','prestamos.png')
foreach ($imagen in $imagenes) {
    if (!(Test-Path -LiteralPath "docs/CAPTURAS/$imagen")) { throw "Falta la captura $imagen" }
}
# Las ocho capturas ya existen. Este script NO modifica el código ni simula errores.
& .\gradlew.bat :shared:testAndroidHostTest :androidApp:installDebug --console=plain
if ($LASTEXITCODE -ne 0) { throw 'Falló la verificación o instalación normal. No se creará el commit.' }
$adb = Join-Path $env:LOCALAPPDATA 'Android/Sdk/platform-tools/adb.exe'
& $adb shell am force-stop pe.edu.upeu.bibliomobil
if ($LASTEXITCODE -ne 0) { throw 'No se pudo reiniciar la aplicación.' }
& $adb shell am start -W -n pe.edu.upeu.bibliomobil/.MainActivity
if ($LASTEXITCODE -ne 0) { throw 'No se pudo abrir la aplicación normal.' }
Add-Content -LiteralPath 'docs/VERIFICACION.md' -Encoding UTF8 -Value "`nCierre ejecutado el $(Get-Date -Format 'yyyy-MM-dd HH:mm'): verificación Android correcta y aplicación normal reinstalada. Ocho capturas completas. Sigue pendiente comprobar iOS en macOS.`n"
& git -c "safe.directory=$PSScriptRoot" add -- docs capturar-estados.ps1
if ($LASTEXITCODE -ne 0) { throw 'No se pudieron preparar las evidencias.' }
& git -c "safe.directory=$PSScriptRoot" diff --cached --quiet
if ($LASTEXITCODE -eq 1) {
    & git -c "safe.directory=$PSScriptRoot" commit -m 'Completa las ocho capturas y restaura la aplicación normal'
    if ($LASTEXITCODE -ne 0) { throw 'No se pudo registrar el commit.' }
}
Write-Host 'ANDROID: aplicación normal instalada y ocho capturas guardadas en Git. iOS: pendiente de comprobación en un Mac.'
