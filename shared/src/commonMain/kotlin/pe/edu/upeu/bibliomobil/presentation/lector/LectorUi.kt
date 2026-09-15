package pe.edu.upeu.bibliomobil.presentation.lector

import pe.edu.upeu.bibliomobil.domain.model.Lector

data class LectorUi(val id: Long, val nombre: String, val correo: String, val telefono: String)
fun Lector.aUi() = LectorUi(id, nombre, correo, telefono ?: "No registrado")
