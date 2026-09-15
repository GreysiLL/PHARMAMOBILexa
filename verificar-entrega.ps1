param([switch]$CrearCommits)
$ErrorActionPreference = 'Stop'
$jbr = Join-Path $env:ProgramFiles 'Android/Android Studio/jbr'
if (Test-Path -LiteralPath (Join-Path $jbr 'bin/java.exe')) { $env:JAVA_HOME = $jbr }
Set-Location -LiteralPath $PSScriptRoot
function Invoke-ProjectGit { & git -c "safe.directory=$PSScriptRoot" @args }
$logPath = Join-Path $PSScriptRoot 'docs/PRUEBAS.log'
& .\gradlew.bat :shared:testAndroidHostTest :androidApp:assembleDebug --console=plain --rerun-tasks 2>&1 | Tee-Object -FilePath $logPath
if ($LASTEXITCODE -ne 0) { throw 'La compilación o las pruebas fallaron. Revisa docs/PRUEBAS.log.' }
$testFiles = @(Get-ChildItem -LiteralPath 'shared/build/test-results/testAndroidHostTest' -Filter 'TEST-*.xml')
if ($testFiles.Count -eq 0) { throw 'No se encontraron resultados de pruebas.' }
$total = 0; $fallos = 0; $omitidas = 0
foreach ($archivo in $testFiles) {
    [xml]$resultado = Get-Content -LiteralPath $archivo.FullName -Raw
    $total += [int]$resultado.testsuite.tests
    $fallos += [int]$resultado.testsuite.failures + [int]$resultado.testsuite.errors
    $omitidas += [int]$resultado.testsuite.skipped
}
if ($total -lt 20 -or $fallos -gt 0 -or $omitidas -gt 0) { throw "Resultados insuficientes: $total pruebas, $fallos fallos, $omitidas omitidas." }
$respuestasPath = Join-Path $PSScriptRoot 'docs/RESPUESTAS.md'
$respuestas = Get-Content -LiteralPath $respuestasPath -Raw -Encoding UTF8
$respuestas = ($respuestas -split '## Verificación de pruebas')[0]
$salida = Get-Content -LiteralPath $logPath -Raw
$respuestas += "## Verificación de pruebas`n`nFecha: $(Get-Date -Format 'yyyy-MM-dd HH:mm'). Pruebas: $total. Fallos: $fallos. Omitidas: $omitidas.`n`n"
$respuestas += '```text' + "`n" + $salida + "`n" + '```' + "`n"
Set-Content -LiteralPath $respuestasPath -Value $respuestas -Encoding UTF8
Set-Content -LiteralPath 'docs/RESULTADO-ANDROID.md' -Encoding UTF8 -Value "# Verificación Android`n`nCompilación y $total pruebas correctas, $fallos fallos, $omitidas omitidas. Ejecutado el $(Get-Date -Format 'yyyy-MM-dd HH:mm').`n"

if ($CrearCommits) {
    $rama = (Invoke-ProjectGit branch --show-current).Trim()
    if ($LASTEXITCODE -ne 0 -or $rama -ne 'feature/clean-mvvm') { throw 'Ejecuta este script en la rama feature/clean-mvvm.' }
    $pendientes = Invoke-ProjectGit diff --cached --name-only
    if ($pendientes) { throw 'Hay cambios ya preparados en Git. Revísalos antes de crear los commits por ítem.' }
    function GuardarItem([string]$mensaje, [string[]]$rutas) {
        Invoke-ProjectGit add -- $rutas
        if ($LASTEXITCODE -ne 0) { throw 'No se pudieron preparar los archivos.' }
        Invoke-ProjectGit diff --cached --quiet
        if ($LASTEXITCODE -eq 1) {
            Invoke-ProjectGit commit -m $mensaje
            if ($LASTEXITCODE -ne 0) { throw 'No se pudo crear el commit.' }
        }
    }
    $comun = 'shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil'
    GuardarItem 'Configura el proyecto multiplataforma del ítem 1' @('.gitignore','build.gradle.kts','settings.gradle.kts','gradle.properties','gradle','gradlew','gradlew.bat','androidApp/build.gradle.kts','androidApp/proguard-rules.pro','androidApp/src/main/res','shared/build.gradle.kts','shared/src/commonMain/composeResources','iosApp/Configuration','iosApp/iosApp.xcodeproj','iosApp/iosApp/Assets.xcassets','iosApp/iosApp/Preview Content','iosApp/iosApp/Info.plist','iosApp/iosApp/ContentView.swift')
    GuardarItem 'Define los modelos y contratos del ítem 2' @("$comun/domain/model","$comun/domain/repository")
    GuardarItem 'Implementa las validaciones y casos de uso del ítem 3' @("$comun/domain/usecase")
    GuardarItem 'Implementa los repositorios en memoria del ítem 4' @("$comun/data")
    GuardarItem 'Construye el módulo de libros del ítem 5' @("$comun/presentation/libro")
    GuardarItem 'Construye el módulo de lectores del ítem 6' @("$comun/presentation/lector")
    GuardarItem 'Conecta Koin en Android e iOS para el ítem 7' @("$comun/di",'shared/src/androidMain','shared/src/iosMain','androidApp/src/main/kotlin','androidApp/src/main/AndroidManifest.xml','iosApp/iosApp/iOSApp.swift')
    GuardarItem 'Añade Inicio y navegación para el ítem 8' @("$comun/App.kt","$comun/navigation","$comun/presentation/inicio")
    GuardarItem 'Define la identidad visual y componentes del ítem 9' @("$comun/theme","$comun/presentation/components")
    GuardarItem 'Verifica los casos del Anexo D para el ítem 10' @('shared/src/commonTest')
    GuardarItem 'Documenta las respuestas y evidencias disponibles' @('docs','README.md','verificar-entrega.ps1')
}
Write-Host "Android verificado: $total pruebas, cero fallos. Sigue pendiente comprobar iOS en un Mac y revisar que estén las ocho capturas."
