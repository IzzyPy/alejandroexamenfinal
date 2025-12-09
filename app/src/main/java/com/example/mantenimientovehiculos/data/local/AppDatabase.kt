// Ruta: data/local/AppDatabase.kt
package com.example.mantenimientovehiculos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// --- INICIO DE LA MODIFICACIÓN ---
// 1. Añadimos LogEntry::class a la lista de entidades.
// 2. Incrementamos la versión de la base de datos a 4 para que Room detecte el cambio.
@Database(entities = [Vehicle::class, LogEntry::class], version = 4, exportSchema = false)
// --- FIN DE LA MODIFICACIÓN ---
abstract class AppDatabase : RoomDatabase() {

    // El DAO para vehículos que ya teníamos.
    abstract fun vehicleDao(): VehicleDao

    // --- INICIO DE LA MODIFICACIÓN ---
    // 3. Declaramos una función para que Room nos proporcione el DAO de los logs.
    abstract fun logDao(): LogDao
    // --- FIN DE LA MODIFICACIÓN ---

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vehicle_database"
                )
                    // Esta línea es clave: si el esquema cambia, Room
                    // destruirá la base de datos y la creará de nuevo.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
