package com.example.examen.appvacacionesfinal.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class LugarVacaciones(
    @PrimaryKey(autoGenerate = true) val id:Int? = null,
    var nombreLugar: String,
    var imagenUri: String,
    var latitud: String,
    var longitud: String,
    var orden: String,
    var costoAlojamiento: String,
    var costoTraslado: String,
    var comentarios: String,
    var costoAlojamientoDolar: String,
    var costoTrasladoDolar: String
) : Serializable
