package pe.edu.upeu.bibliomobil.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val clave: String) {
    data object Inicio : Screen("inicio")
    data object Libros : Screen("libros")
    data object Lectores : Screen("lectores")
    data object Prestamos : Screen("prestamos")
    companion object {
        val saver = Saver<Screen, String>(
            save = { it.clave },
            restore = { clave -> DESTINOS.firstOrNull { it.screen.clave == clave }?.screen ?: Inicio }
        )
    }
}
data class Destino(val screen: Screen, val titulo: String, val icono: ImageVector, val acceso: String? = null, val descripcion: String = "")
val DESTINOS = listOf(
    Destino(Screen.Inicio, "Inicio", Icons.Default.Home),
    Destino(Screen.Libros, "Libros", Icons.Default.MenuBook, "Registrar libros", "Organiza el catálogo de la biblioteca"),
    Destino(Screen.Lectores, "Lectores", Icons.Default.People, "Registrar lectores", "Acerca la lectura a tu comunidad"),
    Destino(Screen.Prestamos, "Préstamos", Icons.Default.Bookmark, "Revisar préstamos", "Conoce el próximo módulo")
)
