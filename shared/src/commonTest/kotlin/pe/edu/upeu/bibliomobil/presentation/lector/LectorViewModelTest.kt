package pe.edu.upeu.bibliomobil.presentation.lector

import kotlin.test.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import pe.edu.upeu.bibliomobil.data.repository.FakeLectorRepository
import pe.edu.upeu.bibliomobil.domain.usecase.*

@OptIn(ExperimentalCoroutinesApi::class)
class LectorViewModelTest {
    @BeforeTest fun preparar() { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    @AfterTest fun limpiar() { Dispatchers.resetMain() }
    @Test fun registraSinTelefono() {
        val repo = FakeLectorRepository()
        val vm = LectorViewModel(RegistrarLectorUseCase(repo), ListarLectoresUseCase(repo))
        vm.onNombreChange("Ana"); vm.onCorreoChange("ana@uni.edu"); vm.registrar()
        val lector = assertIs<LectorUiState.Fase.ConLectores>(vm.uiState.value.fase).lectores.single()
        assertEquals("No registrado", lector.telefono)
        assertEquals(FormularioLector(), vm.uiState.value.formulario)
    }
}
