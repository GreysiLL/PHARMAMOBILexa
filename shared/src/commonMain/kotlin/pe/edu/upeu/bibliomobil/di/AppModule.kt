package pe.edu.upeu.bibliomobil.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.data.repository.LectorRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.repository.*
import pe.edu.upeu.bibliomobil.domain.usecase.*
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel
import pe.edu.upeu.bibliomobil.presentation.lector.LectorViewModel

val dataModule = module {
    single<LibroRepository> { LibroRepositorioEnMemoria() }
    single<LectorRepository> { LectorRepositorioEnMemoria() }
}
val domainModule = module {
    factory { RegistrarLibroUseCase(get()) }
    factory { ListarLibrosUseCase(get()) }
    factory { RegistrarLectorUseCase(get()) }
    factory { ListarLectoresUseCase(get()) }
}
val presentationModule = module {
    viewModel { LibroViewModel(get(), get()) }
    viewModel { LectorViewModel(get(), get()) }
}
expect val platformModule: Module

fun initKoin(configuracionAdicional: KoinApplication.() -> Unit = {}) {
    startKoin {
        configuracionAdicional()
        modules(dataModule, domainModule, presentationModule, platformModule)
    }
}
