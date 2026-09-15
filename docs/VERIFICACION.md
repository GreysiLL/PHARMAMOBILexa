# Estado de verificación

Fecha: 15 de septiembre de 2026.

## Implementado

- Modelos y constantes del Anexo A: Libro con año, Lector y préstamos con días y multa.
- Contratos suspend, cuatro casos de uso, errores por campo y cancelación propagada.
- Repositorios en memoria con Mutex, identificadores correlativos y latencia.
- Formularios, StateFlow, cuatro fases, limpieza y recarga.
- Koin por interfaces y single, casos de uso factory, inicialización Android/iOS.
- Inicio, destinos desde una lista, Saver de navegación y tema propio.
- Paletas completas (incluidos los roles fixed y surfaceContainer) y corrección de contraste de la barra en modo oscuro.
- 48 pruebas automatizadas escritas en commonTest.
- Tres respuestas teóricas propuestas en RESPUESTAS.md.

## Comprobado en el emulador

Con la versión instalada por Android Studio se verificó el registro de libros y lectores, navegación, tema claro/oscuro y pantalla de préstamos. Se registró a Elena (elena@example.org) sin teléfono y se comprobó «No registrado». Se escribió Rayuela, Cortazar, 1998 y 2 ejemplares; al rotar a horizontal y volver se conservaron el destino y los cuatro campos. Dos pulsaciones consecutivas en Registrar incrementaron el catálogo de 1 a 2 libros, y el nuevo libro mostró «Pocos ejemplares». El formulario se limpió después del registro.

Estas comprobaciones corresponden al APK instalado antes de los últimos ajustes de colores. Debe reconstruirse el APK y actualizar las capturas oscuras para reflejar esos ajustes.

## Pendiente: no declarar la entrega completa

1. Ejecutar las 48 pruebas y compilar la última revisión. No hay todavía un reporte de pruebas aprobadas. La sesión de Codex recibe AccessDeniedException al resolver rutas Java incluso con permisos concedidos.
2. Registrar los commits por ítem. La rama feature/clean-mvvm existe, pero la escritura del índice Git está bloqueada para esta sesión.
3. Obtener libros-cargando.png y libros-error.png. La captura de error requiere una simulación temporal que no debe quedar en el código final.
4. Actualizar las capturas oscuras después de compilar la corrección de contraste.
5. Compilar el destino iOS en macOS con Xcode usando ./gradlew :shared:compileKotlinIosSimulatorArm64. No se ha verificado desde Windows.

## Terminar desde la terminal de Android Studio

```powershell
powershell -ExecutionPolicy Bypass -File .\verificar-entrega.ps1 -CrearCommits
```

El script elige el JDK de Android Studio, ejecuta pruebas y compilación, comprueba los XML, pega la salida real en RESPUESTAS.md y crea commits separados por ítem solamente si Android pasa. No publica ningún repositorio. Un fallo detiene el script; debe revisarse docs/PRUEBAS.log. Tras añadir las capturas restantes, registrar un commit adicional de evidencias.
