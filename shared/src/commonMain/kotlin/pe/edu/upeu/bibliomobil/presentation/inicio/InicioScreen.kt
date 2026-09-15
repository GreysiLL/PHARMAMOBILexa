package pe.edu.upeu.bibliomobil.presentation.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.navigation.*

@Composable
fun InicioScreen(onNavegar: (Screen) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.extraLarge) {
            Column(Modifier.fillMaxWidth().padding(28.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.LocalLibrary, null, Modifier.size(52.dp))
                Text("BiblioMobil", style = MaterialTheme.typography.headlineLarge)
                Text("Cada libro abre un mundo.", style = MaterialTheme.typography.titleMedium)
                Text("Biblioteca Central · Tu catálogo, siempre a mano", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text("Qué puedes hacer", style = MaterialTheme.typography.titleLarge)
        DESTINOS.filter { it.acceso != null }.forEach { destino ->
            OutlinedCard(onClick = { onNavegar(destino.screen) }, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(destino.icono, null, tint = MaterialTheme.colorScheme.primary)
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(destino.acceso.orEmpty(), style = MaterialTheme.typography.titleMedium)
                        Text(destino.descripcion, style = MaterialTheme.typography.bodySmall)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null)
                }
            }
        }
    }
}
