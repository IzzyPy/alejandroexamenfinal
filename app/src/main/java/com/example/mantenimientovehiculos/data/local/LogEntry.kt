// Ruta: data/local/LogEntry.kt
package com.example.mantenimientovehiculos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "log_entries")
data class LogEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val message: String, // El mensaje del evento, ej: "Se creó el vehículo Toyota Yaris"

    val timestamp: Long = System.currentTimeMillis() // La fecha y hora en formato numérico (milisegundos)
) {
    // Función de ayuda para mostrar la fecha y hora en un formato legible
    fun getFormattedTimestamp(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
