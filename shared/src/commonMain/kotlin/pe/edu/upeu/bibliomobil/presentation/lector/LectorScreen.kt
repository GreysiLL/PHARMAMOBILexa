package pe.edu.upeu.bibliomobil.presentation.lector

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
fun LectorScreen(viewModel: LectorViewModel, modifier: Modifier = Modifier) {
    val s by viewModel.uiState.collectAsStateWithLifecycle()
    val f = s.formulario
    LazyColumn(modifier.fillMaxSize().imePadding(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Registrar lector", style = MaterialTheme.typography.titleLarge)
                ValidatedTextField(f.nombre, viewModel::onNombreChange, "Nombre", f.nombreError, Modifier.fillMaxWidth(), enabled = !s.registrando)
                ValidatedTextField(f.correo, viewModel::onCorreoChange, "Correo", f.correoError, Modifier.fillMaxWidth(), KeyboardType.Email, !s.registrando)
                ValidatedTextField(f.telefono, viewModel::onTelefonoChange, "Teléfono", f.telefonoError, Modifier.fillMaxWidth(), KeyboardType.Phone, !s.registrando, "Opcional, entre 6 y 9 dígitos")
                Button(onClick = viewModel::registrar, enabled = !s.registrando, modifier = Modifier.fillMaxWidth()) {
                    Text(if (s.registrando) "Registrando…" else "Registrar")
                }
            } }
        }
        s.mensajeExito?.let { mensaje -> item { MensajeExito(mensaje) } }
        item {
            val n = (s.fase as? LectorUiState.Fase.ConLectores)?.lectores?.size ?: 0
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Cartera", style = MaterialTheme.typography.titleMedium)
                Text("$n ${if (n == 1) "lector" else "lectores"}")
            }
        }
        when (val fase = s.fase) {
            LectorUiState.Fase.Cargando -> item {
                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircularProgressIndicator(); Text("Cargando lectores…")
                }
            }
            LectorUiState.Fase.SinLectores -> item { EstadoVacio(Icons.Default.People, "Sin lectores", "Registra el primer lector para comenzar.") }
            is LectorUiState.Fase.Error -> item { EstadoVacio(Icons.Default.ErrorOutline, "Error", fase.mensaje, esError = true, onReintentar = viewModel::cargarLectores) }
            is LectorUiState.Fase.ConLectores -> items(fase.lectores, key = { it.id }) { registro ->
                OutlinedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(registro.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(registro.correo, style = MaterialTheme.typography.bodyMedium)
                        Text(registro.telefono, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
