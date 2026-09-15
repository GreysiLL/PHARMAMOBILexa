package pe.edu.upeu.bibliomobil.domain.usecase

import kotlin.test.*
import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.data.repository.FakeLectorRepository

class RegistrarLectorUseCaseTest {
    private val repo = FakeLectorRepository()
    private val registrar = RegistrarLectorUseCase(repo)
    @Test fun correoInvalido() = runTest {
        val error = assertIs<LectorInvalidoException>(registrar("Ana", "ana@", "").exceptionOrNull())
        assertEquals("El correo no tiene un formato válido", error.errores.correo)
    }
    @Test fun telefonoCorto() = runTest {
        val error = assertIs<LectorInvalidoException>(registrar("Ana", "ana@uni.edu", "123").exceptionOrNull())
        assertEquals("El teléfono debe tener entre 6 y 9 dígitos", error.errores.telefono)
    }
    @Test fun telefonoEnBlancoEsNull() = runTest { assertNull(registrar("Ana", "ana@uni.edu", "  ").getOrThrow().telefono) }
    @Test fun camposObligatorios() = runTest {
        val error = assertIs<LectorInvalidoException>(registrar(" ", "", "").exceptionOrNull())
        assertEquals("El nombre es obligatorio", error.errores.nombre)
        assertEquals("El correo es obligatorio", error.errores.correo)
        assertEquals(0, repo.llamadasRegistrar)
    }
    @Test fun aceptaLimitesTelefono() = runTest {
        assertTrue(registrar("Ana", "ana@uni.edu", "123456").isSuccess)
        assertTrue(registrar("Ana", "ana@uni.edu", "123456789").isSuccess)
    }
    @Test fun rechazaLetrasYTelefonoLargo() = runTest {
        assertTrue(registrar("Ana", "ana@uni.edu", "123abc").isFailure)
        assertTrue(registrar("Ana", "ana@uni.edu", "1234567890").isFailure)
    }
    @Test fun propagaFallo() = runTest { repo.fallo = Exception("Fallo"); assertTrue(registrar("Ana", "ana@uni.edu", "").isFailure) }
}
