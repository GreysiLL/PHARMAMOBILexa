package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

data class ErroresDeLector(val nombre: String? = null, val correo: String? = null, val telefono: String? = null) {
    val hayErrores get() = listOf(nombre, correo, telefono).any { it != null }
}
class LectorInvalidoException(val errores: ErroresDeLector) : IllegalArgumentException("Revisa los datos del lector")

class RegistrarLectorUseCase(private val repository: LectorRepository) {
    suspend operator fun invoke(nombre: String, correo: String, telefono: String): Result<Lector> = resultadoDe {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim()
        val telefonoLimpio = telefono.trim().takeIf { it.isNotEmpty() }
        val errores = ErroresDeLector(
            nombre = if (nombreLimpio.isEmpty()) "El nombre es obligatorio" else null,
            correo = when {
                correoLimpio.isEmpty() -> "El correo es obligatorio"
                !CORREO.matches(correoLimpio) -> "El correo no tiene un formato válido"
                else -> null
            },
            telefono = if (telefonoLimpio != null && !TELEFONO.matches(telefonoLimpio)) "El teléfono debe tener entre 6 y 9 dígitos" else null
        )
        if (errores.hayErrores) throw LectorInvalidoException(errores)
        repository.registrar(Lector(0L, nombreLimpio, correoLimpio, telefonoLimpio))
    }
    private companion object {
        val CORREO = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""")
        val TELEFONO = Regex("[0-9]{6,9}")
    }
}
