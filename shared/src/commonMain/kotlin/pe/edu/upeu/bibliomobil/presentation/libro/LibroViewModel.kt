package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.*

class LibroViewModel(
    private val registrarLibro: RegistrarLibroUseCase,
    private val listarLibros: ListarLibrosUseCase
) : ViewModel() {
    private val estado = MutableStateFlow(LibroUiState())
    val uiState = estado.asStateFlow()
    private var carga: Job? = null
    init { cargarLibros() }

    fun cargarLibros() {
        carga?.cancel()
        estado.update { it.copy(fase = LibroUiState.Fase.Cargando) }
        carga = viewModelScope.launch {
            val fase = listarLibros().fold(
                onSuccess = { lista ->
                    if (lista.isEmpty()) LibroUiState.Fase.SinLibros
                    else LibroUiState.Fase.ConLibros(lista.map { it.aUi() })
                },
                onFailure = { LibroUiState.Fase.Error(it.message ?: "No se pudo cargar el catálogo") }
            )
            estado.update { it.copy(fase = fase) }
        }
    }
    fun onTituloChange(valor: String) {
        if (estado.value.registrando) return
        estado.update { it.copy(formulario = it.formulario.copy(titulo = valor, tituloError = null), mensajeExito = null) }
    }
    fun onAutorChange(valor: String) {
        if (estado.value.registrando) return
        estado.update { it.copy(formulario = it.formulario.copy(autor = valor, autorError = null), mensajeExito = null) }
    }
    fun onAnioChange(valor: String) {
        if (estado.value.registrando) return
        estado.update { it.copy(formulario = it.formulario.copy(anio = valor, anioError = null), mensajeExito = null) }
    }
    fun onEjemplaresChange(valor: String) {
        if (estado.value.registrando) return
        estado.update { it.copy(formulario = it.formulario.copy(ejemplares = valor, ejemplaresError = null), mensajeExito = null) }
    }

    fun registrar() {
        if (estado.value.registrando) return
        val f = estado.value.formulario
        // Se bloquea antes de lanzar la corrutina: dos toques no crean dos registros.
        estado.update { it.copy(registrando = true, mensajeExito = null) }
        viewModelScope.launch {
            try {
                registrarLibro(f.titulo, f.autor, f.anio, f.ejemplares).fold(
                    onSuccess = { registro ->
                        estado.update { it.copy(formulario = FormularioLibro(), mensajeExito = "Libro \"${registro.titulo}\" registrado correctamente") }
                        cargarLibros()
                    },
                    onFailure = { fallo ->
                        if (fallo is LibroInvalidoException) {
                            estado.update { it.copy(formulario = it.formulario.copy(tituloError = fallo.errores.titulo, autorError = fallo.errores.autor, anioError = fallo.errores.anio, ejemplaresError = fallo.errores.ejemplares)) }
                        } else {
                            carga?.cancel()
                            estado.update { it.copy(fase = LibroUiState.Fase.Error(fallo.message ?: "No se pudo registrar el libro")) }
                        }
                    }
                )
            } finally { estado.update { it.copy(registrando = false) } }
        }
    }
}
