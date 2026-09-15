package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class LibroRepositorioEnMemoria : LibroRepository {
    private val mutex = Mutex()
    private val registros = mutableListOf<Libro>()
    private var siguienteId = 1L

    override suspend fun registrar(libro: Libro): Libro {
        delay(400)
        return mutex.withLock {
            libro.copy(id = siguienteId++).also { registros.add(it) }
        }
    }

    override suspend fun listar(): List<Libro> {
        delay(600)
        return mutex.withLock { registros.toList() }
    }
}
