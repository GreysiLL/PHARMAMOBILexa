package pe.edu.upeu.bibliomobil.domain.model

import kotlin.test.*

class DetallePrestamoTest {
    private val libro = Libro(1, "Libro", "Autor", 1998, 3)
    @Test fun rechazaCeroDias() { assertFailsWith<IllegalArgumentException> { DetallePrestamo(libro, 0) } }
    @Test fun rechazaDieciseisDias() { assertFailsWith<IllegalArgumentException> { DetallePrestamo(libro, 16) } }
    @Test fun calculaMulta() { assertEquals(6.0, DetallePrestamo(libro, 15).multaPorRetraso(4)) }
}
