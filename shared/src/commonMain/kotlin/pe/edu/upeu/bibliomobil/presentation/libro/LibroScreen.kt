package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upeu.bibliomobil.presentation.components.*

@Composable
fun LibroScreen(viewModel: LibroViewModel, modifier: Modifier = Modifier) {
    val s by viewModel.uiState.collectAsStateWithLifecycle()
    val f = s.formulario
    LazyColumn(modifier.fillMaxSize().imePadding(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Registrar libro", style = MaterialTheme.typography.titleLarge)
                ValidatedTextField(f.titulo, viewModel::onTituloChange, "Título", f.tituloError, Modifier.fillMaxWidth(), enabled = !s.registrando)
                ValidatedTextField(f.autor, viewModel::onAutorChange, "Autor", f.autorError, Modifier.fillMaxWidth(), enabled = !s.registrando)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ValidatedTextField(f.anio, viewModel::onAnioChange, "Año", f.anioError, Modifier.weight(1f), KeyboardType.Number, !s.registrando)
                    ValidatedTextField(f.ejemplares, viewModel::onEjemplaresChange, "Ejemplares", f.ejemplaresError, Modifier.weight(1f), KeyboardType.Number, !s.registrando)
                }
                Button(onClick = viewModel::registrar, enabled = !s.registrando, modifier = Modifier.fillMaxWidth()) {
                    Text(if (s.registrando) "Registrando…" else "Registrar")
                }
            } }
        }
        s.mensajeExito?.let { mensaje -> item { MensajeExito(mensaje) } }
        item {
            val n = (s.fase as? LibroUiState.Fase.ConLibros)?.libros?.size ?: 0
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Catálogo", style = MaterialTheme.typography.titleMedium)
                Text("$n ${if (n == 1) "libro" else "libros"}")
            }
        }
        when (val fase = s.fase) {
            LibroUiState.Fase.Cargando -> item {
                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircularProgressIndicator(); Text("Cargando libros…")
                }
            }
            LibroUiState.Fase.SinLibros -> item { EstadoVacio(Icons.Default.MenuBook, "Sin libros", "Registra el primer libro para comenzar.") }
            is LibroUiState.Fase.Error -> item { EstadoVacio(Icons.Default.ErrorOutline, "Error", fase.mensaje, esError = true, onReintentar = viewModel::cargarLibros) }
            is LibroUiState.Fase.ConLibros -> items(fase.libros, key = { it.id }) { registro ->
                OutlinedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(registro.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(registro.autor, style = MaterialTheme.typography.bodyMedium)
                        Text(registro.lineaSecundaria, style = MaterialTheme.typography.bodySmall)
                        if (registro.requiereReposicion) Surface(color = MaterialTheme.colorScheme.tertiaryContainer, shape = MaterialTheme.shapes.small) {
                            Text("Pocos ejemplares", Modifier.padding(horizontal = 10.dp, vertical = 5.dp), style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}
