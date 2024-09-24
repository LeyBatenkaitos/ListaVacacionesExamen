package com.example.examen.appvacacionesfinal.ws

import com.squareup.moshi.Json

data class Dolar(
    val codigo:String,
    val nombre:String,
    @Json(name = "unidad_medida")
    val unidad:String,
    @Json(name = "valor")
    val valorCambio:Double
)
