package com.example.examen.appvacacionesfinal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.examen.appvacacionesfinal.dao.LugarVacacionesDAO
import com.example.examen.appvacacionesfinal.entities.LugarVacaciones

@Database(entities = [LugarVacaciones::class], version = 1)
abstract class DatabaseConnection : RoomDatabase () {
    abstract fun LugarVacacionesDAO(): LugarVacacionesDAO

    companion object {

        @Volatile
        private var BASE_DATOS: DatabaseConnection? = null
        fun getInstance(contexto: Context): DatabaseConnection {
            // synchronized previene el acceso de múltiples threads de manera simultánea

            return BASE_DATOS ?: synchronized(this) {
                Room.databaseBuilder(
                    contexto.applicationContext,
                    DatabaseConnection::class.java,
                    "lugarVacaciones.bd"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { BASE_DATOS = it }
            }
        }
    }
}