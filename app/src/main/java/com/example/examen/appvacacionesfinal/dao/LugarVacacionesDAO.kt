package com.example.examen.appvacacionesfinal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.examen.appvacacionesfinal.entities.LugarVacaciones

@Dao
interface LugarVacacionesDAO {

    @Query("SELECT * FROM lugarVacaciones")
    suspend fun getAll(): List<LugarVacaciones>

    @Insert
    suspend fun insertLugarVacaciones(lugarVacaciones: LugarVacaciones): Long

    @Update
    suspend fun updateLugarVacaciones(lugarVacaciones: LugarVacaciones)

    @Delete
    suspend fun deleteLugarVacaciones(lugarVacaciones: LugarVacaciones)
}