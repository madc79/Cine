package com.example.cine.modelo

import java.io.Serializable

class Pelicula(
    val tituloPelicula: String,
    val descripcionPelicula: String,
    val imagenPelicula: String,
    val horario: ArrayList<Horario>,
    val tresD: Boolean,
    val restriccionDeEdad: String
) : Serializable {

}