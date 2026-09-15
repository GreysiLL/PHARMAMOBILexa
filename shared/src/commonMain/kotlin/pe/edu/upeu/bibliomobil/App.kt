package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.bibliomobil.navigation.*
import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio
import pe.edu.upeu.bibliomobil.presentation.inicio.InicioScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorScreen
import pe.edu.upeu.bibliomobil.theme.BiblioMobilTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() = KoinContext {
    val sistemaOscuro = isSystemInDarkTheme()
    var oscuro by rememberSaveable { mutableStateOf(sistemaOscuro) }
    var pantallaActual by rememberSaveable(stateSaver = Screen.saver) { mutableStateOf<Screen>(Screen.Inicio) }
    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    BiblioMobilTheme(oscuro) {
        ModalNavigationDrawer(drawerState = drawer, drawerContent = {
            ModalDrawerSheet {
                Column(Modifier.fillMaxHeight().widthIn(max = 320.dp).padding(16.dp)) {
                    Row(Modifier.padding(vertical = 24.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Default.LocalLibrary, null, tint = MaterialTheme.colorScheme.primary)
                        Text("BiblioMobil", style = MaterialTheme.typography.titleLarge)
                    }
                    Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                        DESTINOS.forEach { destino ->
                            NavigationDrawerItem(label = { Text(destino.titulo) }, icon = { Icon(destino.icono, null) }, selected = pantallaActual == destino.screen,
                                onClick = { pantallaActual = destino.screen; scope.launch { drawer.close() } })
                        }
                    }
                    HorizontalDivider()
                    Row(Modifier.fillMaxWidth().padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Modo oscuro")
                        Switch(checked = oscuro, onCheckedChange = { oscuro = it })
                    }
                }
            }
        }) {
            Scaffold(topBar = {
                TopAppBar(title = { Text(DESTINOS.first { it.screen == pantallaActual }.titulo) },
                    navigationIcon = { IconButton(onClick = { scope.launch { drawer.open() } }) { Icon(Icons.Default.Menu, "Abrir menú") } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer))
            }) { padding ->
                Surface(Modifier.fillMaxSize().padding(padding)) {
                    when (pantallaActual) {
                        Screen.Inicio -> InicioScreen { pantallaActual = it }
                        Screen.Libros -> LibroScreen(koinViewModel())
                        Screen.Lectores -> LectorScreen(koinViewModel())
                        Screen.Prestamos -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            EstadoVacio(Icons.Default.Bookmark, "Préstamos en construcción", "Este módulo llega en una próxima versión de BiblioMobil.")
                        }
                    }
                }
            }
        }
    }
}
