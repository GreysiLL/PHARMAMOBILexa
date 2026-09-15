# Evidencias visuales

Las ocho capturas exigidas están guardadas en esta carpeta y fueron revisadas visualmente. Son imágenes reales del emulador Pixel 9 (2), Android API 36.

- Inicio claro y oscuro.
- Libros: Cargando, Sin libros, Con libros y Error con Reintentar.
- Lectores con teléfono ausente: No registrado.
- Préstamos en construcción.

Las capturas oscuras corresponden a la corrección de contraste. Para obtener Cargando y Error se usó temporalmente una demora y una excepción en listar(), como permite el enunciado. El archivo se restauró exactamente desde el commit aprobado y se comprobó que no tiene diferencias respecto a HEAD. El script capturar-estados.ps1 ahora únicamente verifica, reinstala la aplicación normal y guarda las evidencias; ya no introduce simulaciones.
