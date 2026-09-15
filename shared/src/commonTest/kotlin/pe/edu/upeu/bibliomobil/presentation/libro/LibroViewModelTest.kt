package pe.edu.upeu.bibliomobil.presentation.libro

import kotlin.test.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import pe.edu.upeu.bibliomobil.data.repository.FakeLibroRepository
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomobil.domain.usecase.*

@OptIn(ExperimentalCoroutinesApi::class)
class LibroViewModelTest {
    @BeforeTest fun preparar() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun limpiar() { Dispatchers.resetMain() }
    private fun vm(repo: LibroRepository = FakeLibroRepository()) = LibroViewModel(RegistrarLibroUseCase(repo), ListarLibrosUseCase(repo))
    private fun llenar(vm: LibroViewModel) {
        vm.onTituloChange("Libro"); vm.onAutorChange("Autor"); vm.onAnioChange("1998"); vm.onEjemplaresChange("3")
    }
    @Test fun arrancaSinLibros() { assertIs<LibroUiState.Fase.SinLibros>(vm().uiState.value.fase) }
    @Test fun muestraFormatoExacto() {
        val repo = FakeLibroRepository(); repo.registros.add(Libro(1, "Libro", "Autor", 1998, 3))
        assertEquals("1998 · 3 ejemplares", assertIs<LibroUiState.Fase.ConLibros>(vm(repo).uiState.value.fase).libros.single().lineaSecundaria)
    }
    @Test fun formatoSingular() { assertEquals("1998 · 1 ejemplar", Libro(1, "Libro", "Autor", 1998, 1).aUi().lineaSecundaria) }
    @Test fun cargaFallidaUsaMensajePredeterminado() {
        val repo = FakeLibroRepository(); repo.fallo = Exception()
        assertEquals("No se pudo cargar el catálogo", assertIs<LibroUiState.Fase.Error>(vm(repo).uiState.value.fase).mensaje)
    }
    @Test fun erroresEnFormularioSinCambiarFase() {
        val vm = vm(); vm.registrar()
        assertIs<LibroUiState.Fase.SinLibros>(vm.uiState.value.fase)
        assertEquals("El título es obligatorio", vm.uiState.value.formulario.tituloError)
    }
    @Test fun escribirLimpiaSoloElErrorDelCampo() {
        val vm = vm(); vm.registrar(); vm.onTituloChange("Libro")
        assertNull(vm.uiState.value.formulario.tituloError)
        assertEquals("El autor es obligatorio", vm.uiState.value.formulario.autorError)
    }
    @Test fun registrarLimpiaYRecarga() {
        val vm = vm(); llenar(vm); vm.registrar()
        assertEquals(FormularioLibro(), vm.uiState.value.formulario)
        assertEquals(1, assertIs<LibroUiState.Fase.ConLibros>(vm.uiState.value.fase).libros.size)
        assertEquals("Libro \"Libro\" registrado correctamente", vm.uiState.value.mensajeExito)
        assertFalse(vm.uiState.value.registrando)
    }
    @Test fun reintentarRecuperaCarga() {
        val repo = FakeLibroRepository(); repo.fallo = Exception("Fallo")
        val vm = vm(repo); repo.fallo = null; vm.cargarLibros()
        assertIs<LibroUiState.Fase.SinLibros>(vm.uiState.value.fase)
    }
    @Test fun dobleToqueNoDuplica() = runTest {
        val fake = FakeLibroRepository()
        val lento = object : LibroRepository {
            override suspend fun listar() = fake.listar()
            override suspend fun registrar(libro: Libro): Libro { delay(400); return fake.registrar(libro) }
        }
        val vm = vm(lento); llenar(vm); vm.registrar(); vm.registrar()
        assertTrue(vm.uiState.value.registrando)
        advanceUntilIdle()
        assertEquals(1, fake.llamadasRegistrar); assertFalse(vm.uiState.value.registrando)
    }
}
