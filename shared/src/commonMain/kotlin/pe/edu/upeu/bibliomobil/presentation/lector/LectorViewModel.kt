package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.*

class LectorViewModel(
    private val registrarLector: RegistrarLectorUseCase,
    private val listarLectores: ListarLectoresUseCase
) : ViewModel() {
    private val estado = MutableStateFlow(LectorUiState())
    val uiState = estado.asStateFlow()
    private var carga: Job? = null
    init { cargarLectores() }

    fun cargarLectores() {
        carga?.cancel()
        estado.update { it.copy(fase = LectorUiState.Fase.Cargando) }
        carga = viewModelScope.launch {
            val fase = listarLectores().fold(
                onSuccess = { lista ->
                    if (lista.isEmpty()) LectorUiState.Fase.SinLectores
                    else LectorUiState.Fase.ConLectores(lista.map { it.aUi() })
                },
                onFailure = { LectorUiState.Fase.Error(it.message ?: "No se pudo cargar la cartera de lectores") }
            )
            estado.update { it.copy(fase = fase) }
        }
    }
    fun onNombreChange(valor: String) {
        if (estado.value.registrando) return
        estado.update { it.copy(formulario = it.formulario.copy(nombre = valor, nombreError = null), mensajeExito = null) }
    }
    fun onCorreoChange(valor: String) {
        if (estado.value.registrando) return
        estado.update { it.copy(formulario = it.formulario.copy(correo = valor, correoError = null), mensajeExito = null) }
    }
    fun onTelefonoChange(valor: String) {
        if (estado.value.registrando) return
        estado.update { it.copy(formulario = it.formulario.copy(telefono = valor, telefonoError = null), mensajeExito = null) }
    }

    fun registrar() {
        if (estado.value.registrando) return
        val f = estado.value.formulario
        // Se bloquea antes de lanzar la corrutina: dos toques no crean dos registros.
        estado.update { it.copy(registrando = true, mensajeExito = null) }
        viewModelScope.launch {
            try {
                registrarLector(f.nombre, f.correo, f.telefono).fold(
                    onSuccess = { registro ->
                        estado.update { it.copy(formulario = FormularioLector(), mensajeExito = "Lector \"${registro.nombre}\" registrado correctamente") }
                        cargarLectores()
                    },
                    onFailure = { fallo ->
                        if (fallo is LectorInvalidoException) {
                            estado.update { it.copy(formulario = it.formulario.copy(nombreError = fallo.errores.nombre, correoError = fallo.errores.correo, telefonoError = fallo.errores.telefono)) }
                        } else {
                            carga?.cancel()
                            estado.update { it.copy(fase = LectorUiState.Fase.Error(fallo.message ?: "No se pudo registrar el lector")) }
                        }
                    }
                )
            } finally { estado.update { it.copy(registrando = false) } }
        }
    }
}
