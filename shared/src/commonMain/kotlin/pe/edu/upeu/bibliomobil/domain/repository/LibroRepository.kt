package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro

/** Acceso al registro de libros, independiente del lugar donde se almacena. */
interface LibroRepository {
    /** Registra los datos y devuelve el registro con su identificador asignado. */
    suspend fun registrar(libro: Libro): Libro
    /** Consulta los libros en el orden en que fueron registrados. */
    suspend fun listar(): List<Libro>
}
