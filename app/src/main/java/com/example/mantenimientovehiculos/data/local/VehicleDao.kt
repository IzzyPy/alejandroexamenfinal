// Ruta: data/local/VehicleDao.kt
package com.example.mantenimientovehiculos.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface VehicleDao {

    @Insert
    suspend fun insert(vehicle: Vehicle)

    @Update
    suspend fun update(vehicle: Vehicle)

    @Delete
    suspend fun delete(vehicle: Vehicle)

    @Query("SELECT * FROM vehicles ORDER BY brand ASC")
    fun getAllVehicles(): LiveData<List<Vehicle>>

    @Query("SELECT * FROM vehicles WHERE id = :vehicleId")
    fun getVehicleById(vehicleId: Int): LiveData<Vehicle?>

    @Query("SELECT * FROM vehicles WHERE plate LIKE :query ORDER BY brand ASC")
    fun searchVehiclesByPlate(query: String): LiveData<List<Vehicle>>

    // --- INICIO DE LA MODIFICACIÓN ---
    /**
     * Obtiene un vehículo por su ID una sola vez, sin usar LiveData.
     * Al ser una función 'suspend', se debe llamar desde una corutina.
     * Es ideal para operaciones únicas como obtener el estado 'antiguo' antes de una actualización.
     */
    @Query("SELECT * FROM vehicles WHERE id = :vehicleId")
    suspend fun getVehicleOnce(vehicleId: Int): Vehicle?
    // --- FIN DE LA MODIFICACIÓN ---
}
