package pe.edu.upeu.bibliomobil.di

import kotlin.test.*
import org.koin.dsl.koinApplication
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.repository.*
import pe.edu.upeu.bibliomobil.domain.usecase.*

class AppModuleTest {
    @Test fun resuelveImplementacionPorContrato() {
        val app = koinApplication { modules(dataModule, domainModule) }
        try { assertIs<LibroRepositorioEnMemoria>(app.koin.get<LibroRepository>()) } finally { app.close() }
    }
    @Test fun repositoriosSonUnicos() {
        val app = koinApplication { modules(dataModule, domainModule) }
        try {
            assertSame(app.koin.get<LibroRepository>(), app.koin.get<LibroRepository>())
            assertSame(app.koin.get<LectorRepository>(), app.koin.get<LectorRepository>())
        } finally { app.close() }
    }
    @Test fun resuelveCuatroCasosDeUso() {
        val app = koinApplication { modules(dataModule, domainModule) }
        try {
            assertNotNull(app.koin.get<RegistrarLibroUseCase>()); assertNotNull(app.koin.get<ListarLibrosUseCase>())
            assertNotNull(app.koin.get<RegistrarLectorUseCase>()); assertNotNull(app.koin.get<ListarLectoresUseCase>())
        } finally { app.close() }
    }
}
