# BiblioMobil

Práctica de biblioteca desarrollada sobre un proyecto nuevo generado con el asistente Kotlin Multiplatform de Android Studio. Se usa PharmaMobil como referencia conceptual de las capas y de MVVM; los modelos y las reglas siguen los anexos del caso BiblioMobil.

## Ejecutar

Abrir esta carpeta en Android Studio, sincronizar Gradle, seleccionar `androidApp` y un dispositivo Android y pulsar Run. Se requiere JDK 21 y el SDK configurado por el proyecto. Para iOS se necesita macOS y Xcode; abrir `iosApp/iosApp.xcodeproj`.

```shell
./gradlew :shared:testAndroidHostTest :androidApp:assembleDebug
./gradlew :shared:compileKotlinIosSimulatorArm64
```

## Funciones

- Inicio con tres accesos y navegación lateral que conserva el destino al rotar.
- Libros: título, autor, año de 1450 a 2026 y ejemplares no negativos.
- Lectores: nombre, correo y teléfono opcional de 6 a 9 dígitos.
- Préstamos: modelos de dominio preparados; pantalla en construcción, como pide RF-05.
- Tema azul y ámbar, con modo claro y oscuro.
- Catálogos con estados Cargando, vacío, contenido y Error.

Los registros se guardan en memoria. Se conservan durante la navegación y la rotación, pero se pierden al terminar el proceso. No se implementa persistencia ni backend porque este examen pide repositorios en memoria.

## Organización

`shared/src/commonMain/kotlin/pe/edu/upeu/bibliomobil/` contiene `domain`, `data`, `presentation`, `di`, `navigation` y `theme`. La presentación usa casos de uso, sin importar la capa de datos. Koin conecta los repositorios por interfaz y con alcance `single`.

## Entrega y estudio

- `docs/RESPUESTAS.md`: respuestas propuestas para estudiar y revisar con tus propias palabras, y salida real de pruebas.
- `docs/CAPTURAS/`: evidencias visuales de ejecución.
- `docs/VERIFICACION.md`: estado real de los requisitos y limitaciones de verificación.

La rama de entrega es `feature/clean-mvvm`. El historial conserva un commit por ítem. Repositorio: https://github.com/GreysiLL/PHARMAMOBILexa

- [Informe PDF con portada, explicación y ocho capturas](docs/EXAU1.pdf).
- Android: 48 pruebas aprobadas, cero fallos, según el registro de ejecución incluido.
- La verificación de iOS sigue pendiente de ejecución en macOS con Xcode.
