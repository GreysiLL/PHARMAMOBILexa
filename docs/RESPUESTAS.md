# Parte II · BiblioMobil

## 1. Llegada del backend REST

Añadiría `data/repository/LibroRepositorioRemoto.kt` y `LectorRepositorioRemoto.kt`, junto con el cliente HTTP y los DTO necesarios para convertir las respuestas del servidor en modelos del dominio. Cambiaría `di/AppModule.kt` para enlazar `LibroRepository` y `LectorRepository` con esas implementaciones y actualizaría las dependencias y la configuración de red. Quedarían intactos los modelos de `domain/model`, las interfaces de `domain/repository`, los cuatro casos de uso, los ViewModels y las pantallas: las capas exteriores dependen de los contratos del dominio y el dominio no conoce la fuente de datos. Las pruebas de esas capas pueden seguir usando repositorios falsos; añadiría pruebas para el acceso remoto.

## 2. Año y ejemplares como String

Recibir los campos como `String` permite distinguir un campo vacío, un texto que no representa un entero y un entero fuera del rango permitido, devolviendo el mensaje exacto de cada situación. Si `RegistrarLibroUseCase` recibiera `Int`, la pantalla o el ViewModel tendría que convertir antes de llamar al caso de uso y decidir qué hacer con valores como `""` o `"abc"`; eso repartiría la validación entre capas y podría duplicarla en otras interfaces. Por eso el caso de uso convierte y valida la entrada, mientras que el modelo conserva `anio` y `ejemplares` como enteros válidos.

## 3. Repositorio factory en lugar de single

Con `factory`, Koin crearía un repositorio nuevo cada vez que se resolviera esa dependencia. En esta aplicación, `RegistrarLibroUseCase` y `ListarLibrosUseCase` podrían recibir instancias distintas, cada una con su lista en memoria: el usuario registraría un libro y vería la confirmación, pero al recargar el catálogo ese libro no aparecería. Con `single`, ambos casos de uso comparten la misma instancia y consultan los mismos registros mientras vive el proceso de la aplicación; esto no equivale a guardar los datos permanentemente en disco.

## Verificación de pruebas

Fecha: 2026-09-15 14:58. Pruebas: 48. Fallos: 0. Omitidas: 0.

```text
Starting a Gradle Daemon, 1 incompatible Daemon could not be reused, use --status for details
Calculating task graph as no cached configuration is available for tasks: :shared:testAndroidHostTest :androidApp:assembleDebug

> Configure project :shared
w: Native task 'iosSimulatorArm64Test' is disabled
Task 'iosSimulatorArm64Test' for target 'ios_simulator_arm64' cannot run on the current host (windows-x86_64).
Reason: simulator tests require macOS
Solution: To suppress this warning, add 'kotlin.native.ignoreDisabledTargets=true' to gradle.properties.


> Task :shared:generateAndroidHostTestAssets UP-TO-DATE
> Task :shared:generateAndroidMainAssets UP-TO-DATE
> Task :shared:androidPreBuild UP-TO-DATE
> Task :shared:preAndroidMainBuild UP-TO-DATE
> Task :shared:preAndroidHostTestBuild UP-TO-DATE
> Task :androidApp:preBuild UP-TO-DATE
> Task :shared:convertXmlValueResourcesForAndroidMain NO-SOURCE
> Task :shared:processAndroidHostTestJavaRes NO-SOURCE
> Task :shared:convertXmlValueResourcesForCommonTest NO-SOURCE
> Task :shared:copyNonXmlValueResourcesForAndroidMain NO-SOURCE
> Task :shared:copyNonXmlValueResourcesForCommonTest NO-SOURCE
> Task :shared:convertXmlValueResourcesForAndroidHostTest NO-SOURCE
> Task :shared:kmpPartiallyResolvedDependenciesChecker
> Task :shared:checkKotlinGradlePluginConfigurationErrors SKIPPED
> Task :shared:prepareComposeResourcesTaskForAndroidMain NO-SOURCE
> Task :shared:prepareComposeResourcesTaskForCommonTest NO-SOURCE
> Task :androidApp:preDebugBuild UP-TO-DATE
> Task :shared:copyNonXmlValueResourcesForAndroidHostTest NO-SOURCE
> Task :shared:generateResourceAccessorsForCommonTest NO-SOURCE
> Task :shared:generateResourceAccessorsForAndroidMain NO-SOURCE
> Task :androidApp:mergeDebugNativeDebugMetadata NO-SOURCE
> Task :androidApp:generateDebugAssets UP-TO-DATE
> Task :shared:prepareComposeResourcesTaskForAndroidHostTest NO-SOURCE
> Task :shared:generateResourceAccessorsForAndroidHostTest NO-SOURCE
> Task :shared:copyNonXmlValueResourcesForCommonMain
> Task :shared:convertXmlValueResourcesForCommonMain NO-SOURCE
> Task :shared:generateAndroidMainEmptyResourceFiles
> Task :shared:prepareComposeResourcesTaskForCommonMain
> Task :shared:copyAndroidMainComposeResourcesToAndroidAssets
> Task :androidApp:generateDebugResources
> Task :shared:generateAndroidHostTestResources
> Task :androidApp:desugarDebugFileDependencies
> Task :shared:androidJar
> Task :androidApp:createDebugCompatibleScreenManifests
> Task :androidApp:extractDeepLinksDebug
> Task :shared:generateComposeResClass
> Task :shared:generateExpectResourceCollectorsForCommonMain
> Task :shared:generateResourceAccessorsForCommonMain
> Task :shared:generateActualResourceCollectorsForAndroidMain
> Task :androidApp:writeDebugAppMetadata
> Task :shared:packageAndroidMainResources
> Task :androidApp:writeDebugSigningConfigVersions
> Task :androidApp:validateSigningDebug
> Task :shared:parseAndroidMainLocalResources
> Task :androidApp:packageDebugResources
> Task :shared:generateAndroidMainRFile
> Task :shared:mergeAndroidMainJniLibFolders
> Task :androidApp:javaPreCompileDebug
> Task :shared:writeAndroidMainAarMetadata
> Task :androidApp:mergeDebugJniLibFolders
> Task :shared:processAndroidMainManifest
> Task :shared:mergeAndroidMainAssets
> Task :shared:compileAndroidMainLibraryResources
> Task :shared:mapAndroidHostTestSourceSetPaths
> Task :androidApp:mapDebugSourceSetPaths
> Task :shared:mergeAndroidMainNativeLibs NO-SOURCE
> Task :shared:copyAndroidMainJniLibsProjectOnly
> Task :androidApp:checkDebugDuplicateClasses
> Task :androidApp:checkDebugAarMetadata
> Task :shared:mergeAndroidHostTestAssets
> Task :shared:checkAndroidHostTestAarMetadata
> Task :androidApp:processDebugNavigationResources
> Task :androidApp:parseDebugLocalResources
> Task :androidApp:compileDebugNavigationResources
> Task :androidApp:generateDebugRFile
> Task :androidApp:mergeDebugAssets
> Task :androidApp:mergeDebugNativeLibs
> Task :androidApp:compressDebugAssets

> Task :androidApp:stripDebugDebugSymbols
Unable to strip the following libraries, packaging them as they are: libandroidx.graphics.path.so. Run with --info option to learn more.

> Task :shared:processAndroidHostTestManifest
> Task :androidApp:processDebugMainManifest
> Task :androidApp:processDebugManifest
> Task :androidApp:processDebugManifestForPackage
> Task :androidApp:mergeDebugResources
> Task :shared:mergeAndroidHostTestResources
> Task :shared:processAndroidHostTestResources
> Task :androidApp:processDebugResources
> Task :shared:packageAndroidHostTestForUnitTest
> Task :shared:generateAndroidHostTestConfig

> Task :shared:compileAndroidMain
w: file:///C:/Users/HECTOR/AndroidStudioProjects/BiblioMobil/shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/App.kt:27:13 'fun KoinContext(koin: Koin = ..., content: ComposableFunction0<Unit>): Unit' is deprecated. KoinContext is not needed anymore. This can be removed. Compose Koin context is setup with StartKoin().
w: file:///C:/Users/HECTOR/AndroidStudioProjects/BiblioMobil/shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/navigation/Screen.kt:23:52 'val Icons.Filled.MenuBook: ImageVector' is deprecated. Use the AutoMirrored version at Icons.AutoMirrored.Filled.MenuBook.
w: file:///C:/Users/HECTOR/AndroidStudioProjects/BiblioMobil/shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/presentation/libro/LibroScreen.kt:50:77 'val Icons.Filled.MenuBook: ImageVector' is deprecated. Use the AutoMirrored version at Icons.AutoMirrored.Filled.MenuBook.

> Task :shared:processAndroidMainJavaRes
> Task :shared:bundleAndroidMainClassesToCompileJar
> Task :shared:bundleAndroidMainClassesToRuntimeJar
> Task :shared:bundleLibRuntimeToDirAndroidMain
> Task :androidApp:compileDebugKotlin
> Task :androidApp:compileDebugJavaWithJavac NO-SOURCE
> Task :androidApp:processDebugJavaRes
> Task :androidApp:dexBuilderDebug
> Task :androidApp:mergeProjectDexDebug
> Task :shared:compileAndroidHostTest
> Task :androidApp:mergeExtDexDebug
> Task :androidApp:mergeLibDexDebug

> Task :shared:testAndroidHostTest
PRUEBAS: 48; FALLOS: 0; OMITIDAS: 0

> Task :androidApp:mergeDebugJavaResource
> Task :androidApp:packageDebug
> Task :androidApp:assembleDebug
> Task :androidApp:createDebugApkListingFileRedirect

[Incubating] Problems report is available at: file:///C:/Users/HECTOR/AndroidStudioProjects/BiblioMobil/build/reports/problems/problems-report.html

BUILD SUCCESSFUL in 1m 10s
70 actionable tasks: 70 executed
Configuration cache entry stored.

```

