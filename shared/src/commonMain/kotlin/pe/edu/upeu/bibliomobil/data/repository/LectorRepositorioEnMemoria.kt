package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

class LectorRepositorioEnMemoria : LectorRepository {
    private val mutex = Mutex()
    private val registros = mutableListOf<Lector>()
    private var siguienteId = 1L

    override suspend fun registrar(lector: Lector): Lector {
        delay(400)
        return mutex.withLock {
            lector.copy(id = siguienteId++).also { registros.add(it) }
        }
    }

    override suspend fun listar(): List<Lector> {
        delay(600)
        return mutex.withLock { registros.toList() }
    }
}
