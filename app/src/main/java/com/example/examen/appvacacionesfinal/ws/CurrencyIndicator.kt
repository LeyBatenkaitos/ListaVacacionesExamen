package com.example.examen.appvacacionesfinal.ws

import com.squareup.moshi.Json

data class CurrencyIndicator(
    @Json(name = "dolar")
    val dolarCurrency: Dolar
)
