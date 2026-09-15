package pe.edu.upeu.bibliomobil.domain.usecase

import kotlin.test.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.data.repository.FakeLibroRepository

class RegistrarLibroUseCaseTest {
    private val repo = FakeLibroRepository()
    private val registrar = RegistrarLibroUseCase(repo)
    @Test fun aceptaYRecorta() = runTest {
        val libro = registrar("  Rayuela ", " Cortázar ", "1998", "3").getOrThrow()
        assertEquals("Rayuela", libro.titulo); assertEquals("Cortázar", libro.autor)
    }
    @Test fun enviaCeroYRepositorioAsignaId() = runTest {
        val libro = registrar("Libro", "Autor", "1998", "3").getOrThrow()
        assertEquals(0L, repo.recibido?.id); assertEquals(1L, libro.id)
    }
    @Test fun tituloObligatorio() = runTest { assertEquals("El título es obligatorio", errores(titulo = " ").titulo) }
    @Test fun autorObligatorio() = runTest { assertEquals("El autor es obligatorio", errores(autor = "").autor) }
    @Test fun anioObligatorio() = runTest { assertEquals("El año es obligatorio", errores(anio = " ").anio) }
    @Test fun anioEntero() = runTest { assertEquals("El año debe ser un número entero", errores(anio = "abc").anio) }
    @Test fun anioRango() = runTest { assertEquals("El año debe estar entre 1450 y 2026", errores(anio = "1449").anio) }
    @Test fun ejemplaresObligatorios() = runTest { assertEquals("Los ejemplares son obligatorios", errores(ejemplares = "").ejemplares) }
    @Test fun ejemplaresEnteros() = runTest { assertEquals("Los ejemplares deben ser un número entero", errores(ejemplares = "1.5").ejemplares) }
    @Test fun ejemplaresNoNegativos() = runTest { assertEquals("Los ejemplares no pueden ser negativos", errores(ejemplares = "-1").ejemplares) }
    @Test fun invalidoNoLlamaRepositorio() = runTest { errores(titulo = ""); assertEquals(0, repo.llamadasRegistrar) }
    @Test fun permiteCeroEjemplares() = runTest { assertEquals(0, registrar("Libro", "Autor", "2026", "0").getOrThrow().ejemplares) }
    @Test fun devuelveFalloDelRepositorio() = runTest {
        val fallo = IllegalStateException("Sin conexión"); repo.fallo = fallo
        assertSame(fallo, registrar("Libro", "Autor", "1998", "3").exceptionOrNull())
    }
    @Test fun relanzaCancelacion() = runTest {
        repo.fallo = CancellationException("Cancelado")
        assertFailsWith<CancellationException> { registrar("Libro", "Autor", "1998", "3") }
    }
    private suspend fun errores(titulo: String = "Libro", autor: String = "Autor", anio: String = "1998", ejemplares: String = "3") =
        assertIs<LibroInvalidoException>(registrar(titulo, autor, anio, ejemplares).exceptionOrNull()).errores
}
