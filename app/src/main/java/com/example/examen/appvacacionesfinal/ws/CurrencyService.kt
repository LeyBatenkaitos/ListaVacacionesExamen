package com.example.examen.appvacacionesfinal.ws

import retrofit2.http.GET

interface CurrencyService {
    // https://mindicador.cl/api
    @GET("/api")
    suspend fun getCurrency(): CurrencyIndicator

}