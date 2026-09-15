package pe.edu.upeu.bibliomobil.data.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class FakeLibroRepository : LibroRepository {
    val registros = mutableListOf<Libro>()
    var fallo: Exception? = null
    var recibido: Libro? = null
    var llamadasRegistrar = 0
    override suspend fun registrar(libro: Libro): Libro {
        llamadasRegistrar++
        fallo?.let { throw it }
        recibido = libro
        return libro.copy(id = registros.size + 1L).also { registros.add(it) }
    }
    override suspend fun listar(): List<Libro> {
        fallo?.let { throw it }
        return registros.toList()
    }
}
