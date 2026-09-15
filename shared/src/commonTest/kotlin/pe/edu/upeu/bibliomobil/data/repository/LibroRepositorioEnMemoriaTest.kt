package pe.edu.upeu.bibliomobil.data.repository

import kotlin.test.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.domain.model.Libro

class LibroRepositorioEnMemoriaTest {
    private val repo = LibroRepositorioEnMemoria()
    private val libro = Libro(0, "Libro", "Autor", 1998, 3)
    @Test fun idsCorrelativos() = runTest { assertEquals(1L, repo.registrar(libro).id); assertEquals(2L, repo.registrar(libro).id) }
    @Test fun listaEnOrden() = runTest {
        repo.registrar(libro.copy(titulo = "Primero")); repo.registrar(libro.copy(titulo = "Segundo"))
        assertEquals(listOf("Primero", "Segundo"), repo.listar().map { it.titulo })
    }
    @Test fun registrosConcurrentesSinIdsRepetidos() = runTest {
        val resultados = (1..10).map { async { repo.registrar(libro) } }.awaitAll()
        assertEquals(10, resultados.map { it.id }.toSet().size)
        assertEquals(10, repo.listar().size)
    }
}
