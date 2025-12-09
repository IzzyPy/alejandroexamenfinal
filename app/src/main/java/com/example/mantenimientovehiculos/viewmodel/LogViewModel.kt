// Ruta: app/src/main/java/com/example/mantenimientovehiculos/viewmodel/LogViewModel.kt
package com.example.mantenimientovehiculos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mantenimientovehiculos.data.VehicleRepository
import com.example.mantenimientovehiculos.data.local.AppDatabase
import com.example.mantenimientovehiculos.data.local.LogEntry

class LogViewModel(application: Application) : AndroidViewModel(application) {

    // Repositorio para acceder a los datos.
    private val repository: VehicleRepository

    // LiveData que la UI observará para obtener la lista de logs.
    val allLogs: LiveData<List<LogEntry>>

    init {
        // Obtenemos una instancia de la base de datos.
        val database = AppDatabase.getDatabase(application)

        // Creamos el repositorio pasándole ambos DAOs.
        repository = VehicleRepository(database.vehicleDao(), database.logDao())

        // Asignamos la lista de logs del repositorio a nuestra propiedad LiveData.
        allLogs = repository.allLogs
    }
}
