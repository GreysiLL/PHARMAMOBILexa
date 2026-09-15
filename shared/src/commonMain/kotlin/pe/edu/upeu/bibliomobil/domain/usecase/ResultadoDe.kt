package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.CancellationException

/** Convierte fallos en Result sin interrumpir la cancelación estructurada. */
suspend fun <T> resultadoDe(bloque: suspend () -> T): Result<T> = try {
    Result.success(bloque())
} catch (cancelacion: CancellationException) {
    throw cancelacion
} catch (fallo: Exception) {
    Result.failure(fallo)
}
