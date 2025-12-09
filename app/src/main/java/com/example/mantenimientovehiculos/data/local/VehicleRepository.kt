// Ruta: data/VehicleRepository.kt
package com.example.mantenimientovehiculos.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mantenimientovehiculos.data.local.LogDao
import com.example.mantenimientovehiculos.data.local.LogEntry
import com.example.mantenimientovehiculos.data.local.Vehicle
import com.example.mantenimientovehiculos.data.local.VehicleDao
import com.example.mantenimientovehiculos.data.remote.RetrofitClient

class VehicleRepository(private val vehicleDao: VehicleDao, private val logDao: LogDao) {

    val allVehicles: LiveData<List<Vehicle>> = vehicleDao.getAllVehicles()
    val allLogs: LiveData<List<LogEntry>> = logDao.getAllLogs()
    private val vehicleApiService = RetrofitClient.apiService

    private suspend fun insertLog(message: String) {
        logDao.insert(LogEntry(message = message))
    }

    suspend fun sendVehicleToServer(vehicle: Vehicle) {
        val fullUrl = "https://webhook.site/5c949c5a-c669-4c21-8730-0657fa9ad4ea"
        try {
            val response = vehicleApiService.sendVehicle(fullUrl, vehicle)
            if (response.isSuccessful) {
                val logMessage = "Sincronización exitosa: ${vehicle.brand} ${vehicle.model} (${vehicle.plate})."
                Log.d("VehicleRepository", logMessage)
                insertLog(logMessage)
            } else {
                val logMessage = "Error de sincronización (${response.code()}) para: ${vehicle.brand} ${vehicle.model}."
                Log.e("VehicleRepository", logMessage)
                insertLog(logMessage)
            }
        } catch (e: Exception) {
            val logMessage = "Fallo de red al sincronizar: ${e.message}"
            Log.e("VehicleRepository", logMessage)
            insertLog(logMessage)
        }
    }

    suspend fun insert(vehicle: Vehicle) {
        vehicleDao.insert(vehicle)
        insertLog("Creado: ${vehicle.brand} ${vehicle.model} (${vehicle.plate}).")
    }

    suspend fun update(oldVehicle: Vehicle, newVehicle: Vehicle) {
        vehicleDao.update(newVehicle)
        val changes = newVehicle.getChanges(oldVehicle)
        if (changes.isNotEmpty()) {
            val changesString = changes.joinToString(separator = ", ")
            insertLog("Modificado: ${newVehicle.brand} ${newVehicle.model}. Cambios: $changesString.")
        } else {
            // Este es el mensaje que queremos evitar que se guarde incorrectamente.
            // insertLog("Re-guardado sin cambios: ${newVehicle.brand} ${newVehicle.model}.")
        }
    }

    suspend fun delete(vehicle: Vehicle) {
        vehicleDao.delete(vehicle)
        insertLog("Borrado: ${vehicle.brand} ${vehicle.model} (${vehicle.plate}).")
    }

    fun getVehicleById(id: Int): LiveData<Vehicle?> {
        return vehicleDao.getVehicleById(id)
    }

    fun searchVehiclesByPlate(query: String): LiveData<List<Vehicle>> {
        return vehicleDao.searchVehiclesByPlate(query)
    }

    // --- INICIO DE LA MODIFICACIÓN ---
    /**
     * Llama al DAO para obtener un vehículo por su ID una sola vez, sin usar LiveData.
     */
    suspend fun getVehicleOnce(id: Int): Vehicle? {
        return vehicleDao.getVehicleOnce(id)
    }
    // --- FIN DE LA MODIFICACIÓN ---
}
