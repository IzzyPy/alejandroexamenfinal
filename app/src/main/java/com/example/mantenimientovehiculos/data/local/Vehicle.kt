// Ruta: data/local/Vehicle.kt
package com.example.mantenimientovehiculos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Clave primaria que se genera sola
    val brand: String, // Marca del vehículo
    val model: String, // Modelo del vehículo
    val year: Int,     // Año del vehículo
    val plate: String, // Patente o matrícula
    val imagePath: String? = null, // Ruta de la foto (puede ser nula)

    // Kilometraje para el mantenimiento del MOTOR
    val engineLastKm: Int,
    val engineNextKm: Int,

    // Kilometraje para el mantenimiento de la CAJA (transmisión)
    val transmissionLastKm: Int,
    val transmissionNextKm: Int
) : Serializable {

    // --- INICIO DE LA MODIFICACIÓN ---
    /**
     * Compara este vehículo (el estado nuevo) con una versión anterior (oldVehicle)
     * y devuelve una lista de strings describiendo cada campo que ha cambiado.
     */
    fun getChanges(oldVehicle: Vehicle): List<String> {
        val changes = mutableListOf<String>()

        if (this.brand != oldVehicle.brand) {
            changes.add("Marca: '${oldVehicle.brand}' -> '${this.brand}'")
        }
        if (this.model != oldVehicle.model) {
            changes.add("Modelo: '${oldVehicle.model}' -> '${this.model}'")
        }
        if (this.year != oldVehicle.year) {
            changes.add("Año: ${oldVehicle.year} -> ${this.year}")
        }
        if (this.plate != oldVehicle.plate) {
            changes.add("Patente: '${oldVehicle.plate}' -> '${this.plate}'")
        }
        if (this.engineLastKm != oldVehicle.engineLastKm) {
            changes.add("Km Motor (Último): ${oldVehicle.engineLastKm} -> ${this.engineLastKm}")
        }
        if (this.engineNextKm != oldVehicle.engineNextKm) {
            changes.add("Km Motor (Próximo): ${oldVehicle.engineNextKm} -> ${this.engineNextKm}")
        }
        if (this.transmissionLastKm != oldVehicle.transmissionLastKm) {
            changes.add("Km Caja (Último): ${oldVehicle.transmissionLastKm} -> ${this.transmissionLastKm}")
        }
        if (this.transmissionNextKm != oldVehicle.transmissionNextKm) {
            changes.add("Km Caja (Próximo): ${oldVehicle.transmissionNextKm} -> ${this.transmissionNextKm}")
        }
        // No comparamos la ruta de la imagen para simplificar, pero se podría añadir si fuese necesario.

        return changes
    }
    // --- FIN DE LA MODIFICACIÓN ---
}
