package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/** Acceso al registro de lectores, independiente del lugar donde se almacena. */
interface LectorRepository {
    /** Registra los datos y devuelve el registro con su identificador asignado. */
    suspend fun registrar(lector: Lector): Lector
    /** Consulta los lectores en el orden en que fueron registrados. */
    suspend fun listar(): List<Lector>
}
