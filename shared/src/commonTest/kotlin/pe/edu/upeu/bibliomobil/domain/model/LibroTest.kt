package pe.edu.upeu.bibliomobil.domain.model

import kotlin.test.*

class LibroTest {
    private fun libro(anio: Int = 1998, ejemplares: Int = 3) = Libro(0, "Libro", "Autor", anio, ejemplares)
    @Test fun rechazaTituloVacio() { assertFailsWith<IllegalArgumentException> { Libro(0, " ", "Autor", 1998, 3) } }
    @Test fun rechazaAutorVacio() { assertFailsWith<IllegalArgumentException> { Libro(0, "Libro", " ", 1998, 3) } }
    @Test fun rechazaAnioInferior() { assertFailsWith<IllegalArgumentException> { libro(anio = 1449) } }
    @Test fun rechazaAnioSuperior() { assertFailsWith<IllegalArgumentException> { libro(anio = 2027) } }
    @Test fun aceptaLimitesDelAnio() { assertEquals(1450, libro(1450).anio); assertEquals(2026, libro(2026).anio) }
    @Test fun reposicionConDos() { assertTrue(libro(ejemplares = 2).requiereReposicion) }
    @Test fun sinReposicionConTres() { assertFalse(libro(ejemplares = 3).requiereReposicion) }
    @Test fun rechazaEjemplaresNegativos() { assertFailsWith<IllegalArgumentException> { libro(ejemplares = -1) } }
}
