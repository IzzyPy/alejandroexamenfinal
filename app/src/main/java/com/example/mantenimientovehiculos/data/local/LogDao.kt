// Ruta: data/local/LogDao.kt
package com.example.mantenimientovehiculos.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LogDao {

    /**
     * Inserta una nueva entrada de log en la base de datos.
     * Si hay un conflicto, la estrategia IGNORE no hace nada (aunque con un autoincrement no debería pasar).
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(logEntry: LogEntry)

    /**
     * Obtiene todas las entradas de log de la base de datos, ordenadas por fecha y hora descendente (las más nuevas primero).
     * Devuelve LiveData para que la UI se actualice automáticamente si se añade un nuevo log.
     */
    @Query("SELECT * FROM log_entries ORDER BY timestamp DESC")
    fun getAllLogs(): LiveData<List<LogEntry>>

}
