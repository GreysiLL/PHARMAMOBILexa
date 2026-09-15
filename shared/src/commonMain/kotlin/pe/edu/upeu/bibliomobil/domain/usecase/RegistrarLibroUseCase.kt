package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

data class ErroresDeLibro(val titulo: String? = null, val autor: String? = null, val anio: String? = null, val ejemplares: String? = null) {
    val hayErrores get() = listOf(titulo, autor, anio, ejemplares).any { it != null }
}
class LibroInvalidoException(val errores: ErroresDeLibro) : IllegalArgumentException("Revisa los datos del libro")

class RegistrarLibroUseCase(private val repository: LibroRepository) {
    suspend operator fun invoke(titulo: String, autor: String, anio: String, ejemplares: String): Result<Libro> = resultadoDe {
        val tituloLimpio = titulo.trim()
        val autorLimpio = autor.trim()
        val numeroAnio = anio.trim().toIntOrNull()
        val numeroEjemplares = ejemplares.trim().toIntOrNull()
        val errores = ErroresDeLibro(
            titulo = if (tituloLimpio.isEmpty()) "El título es obligatorio" else null,
            autor = if (autorLimpio.isEmpty()) "El autor es obligatorio" else null,
            anio = when {
                anio.isBlank() -> "El año es obligatorio"
                numeroAnio == null -> "El año debe ser un número entero"
                numeroAnio !in Libro.ANIO_MINIMO..Libro.ANIO_MAXIMO -> "El año debe estar entre 1450 y 2026"
                else -> null
            },
            ejemplares = when {
                ejemplares.isBlank() -> "Los ejemplares son obligatorios"
                numeroEjemplares == null -> "Los ejemplares deben ser un número entero"
                numeroEjemplares < 0 -> "Los ejemplares no pueden ser negativos"
                else -> null
            }
        )
        if (errores.hayErrores) throw LibroInvalidoException(errores)
        repository.registrar(Libro(0L, tituloLimpio, autorLimpio, requireNotNull(numeroAnio), requireNotNull(numeroEjemplares)))
    }
}
