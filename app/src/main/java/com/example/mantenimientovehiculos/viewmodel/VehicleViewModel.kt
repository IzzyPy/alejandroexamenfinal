package com.example.mantenimientovehiculos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.mantenimientovehiculos.data.VehicleRepository
import com.example.mantenimientovehiculos.data.local.AppDatabase
import com.example.mantenimientovehiculos.data.local.Vehicle
import kotlinx.coroutines.launch

class VehicleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VehicleRepository = run {
        val database = AppDatabase.getDatabase(application)
        VehicleRepository(database.vehicleDao(), database.logDao())
    }

    // --- Lógica de Búsqueda ---
    private val searchQuery = MutableLiveData<String>("")

    val allVehicles: LiveData<List<Vehicle>> = searchQuery.switchMap { query ->
        if (query.isNullOrEmpty()) {
            repository.allVehicles
        } else {
            repository.searchVehiclesByPlate("%$query%")
        }
    }

    /**
     * Función para que la UI (MainActivity) pueda actualizar el texto de búsqueda.
     */
    fun search(query: String) {
        searchQuery.value = query
    }

    fun insert(vehicle: Vehicle) = viewModelScope.launch {
        repository.insert(vehicle)
    }

    /**
     * Llama al repositorio para actualizar un vehículo, pasándole tanto la versión
     * antigua como la nueva para poder registrar los cambios.
     */
    fun update(oldVehicle: Vehicle, newVehicle: Vehicle) = viewModelScope.launch {
        repository.update(oldVehicle, newVehicle)
    }

    fun delete(vehicle: Vehicle) = viewModelScope.launch {
        repository.delete(vehicle)
    }

    fun getVehicleById(id: Int): LiveData<Vehicle?> {
        return repository.getVehicleById(id)
    }

    fun sendVehicle(vehicle: Vehicle) = viewModelScope.launch {
        repository.sendVehicleToServer(vehicle)
    }

    // --- INICIO DE LA MODIFICACIÓN ---
    /**
     * Llama al repositorio para obtener un vehículo por su ID una sola vez, sin usar LiveData.
     * Es una función 'suspend' para ser llamada desde una corutina.
     */
    suspend fun getVehicleOnce(id: Int): Vehicle? {
        return repository.getVehicleOnce(id)
    }
    // --- FIN DE LA MODIFICACIÓN ---
}
