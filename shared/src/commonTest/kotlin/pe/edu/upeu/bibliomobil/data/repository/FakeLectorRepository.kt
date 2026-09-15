package pe.edu.upeu.bibliomobil.data.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

class FakeLectorRepository : LectorRepository {
    val registros = mutableListOf<Lector>()
    var fallo: Exception? = null
    var recibido: Lector? = null
    var llamadasRegistrar = 0
    override suspend fun registrar(lector: Lector): Lector {
        llamadasRegistrar++
        fallo?.let { throw it }
        recibido = lector
        return lector.copy(id = registros.size + 1L).also { registros.add(it) }
    }
    override suspend fun listar(): List<Lector> {
        fallo?.let { throw it }
        return registros.toList()
    }
}
