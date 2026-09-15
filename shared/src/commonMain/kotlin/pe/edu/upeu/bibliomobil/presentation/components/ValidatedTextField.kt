package pe.edu.upeu.bibliomobil.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun ValidatedTextField(value: String, onValueChange: (String) -> Unit, label: String,
    error: String?, modifier: Modifier = Modifier, keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true, ayuda: String? = null) {
    OutlinedTextField(value = value, onValueChange = onValueChange, label = { Text(label) },
        modifier = modifier, enabled = enabled, singleLine = true, isError = error != null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        supportingText = if (error != null || ayuda != null) { { Text(error ?: ayuda.orEmpty()) } } else null)
}
