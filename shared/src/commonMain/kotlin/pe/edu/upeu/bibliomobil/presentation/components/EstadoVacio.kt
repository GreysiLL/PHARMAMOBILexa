package pe.edu.upeu.bibliomobil.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun EstadoVacio(icono: ImageVector, titulo: String, descripcion: String, modifier: Modifier = Modifier,
    esError: Boolean = false, onReintentar: (() -> Unit)? = null) {
    val color = if (esError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
    Column(modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(icono, null, Modifier.size(48.dp), tint = color)
        Text(titulo, style = MaterialTheme.typography.titleMedium, color = color)
        Text(descripcion, style = MaterialTheme.typography.bodyMedium, color = color)
        if (onReintentar != null) OutlinedButton(onClick = onReintentar) { Text("Reintentar") }
    }
}
