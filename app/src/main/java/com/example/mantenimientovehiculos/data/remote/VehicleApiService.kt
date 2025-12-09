// Ruta: data/remote/VehicleApiService.kt
package com.example.mantenimientovehiculos.data.remote

import com.example.mantenimientovehiculos.data.local.Vehicle
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url // <-- Importación añadida

interface VehicleApiService {

    // --- CAMBIO RECOMENDADO ---
    // Usamos @Url para poder pasar la URL completa y dinámica desde el Repository.
    // El @POST se deja sin parámetros porque la URL ya contendrá todo lo necesario.
    @POST
    suspend fun sendVehicle(
        @Url url: String, // <-- Nuevo parámetro para la URL completa
        @Body vehicle: Vehicle
    ): Response<Unit>
}


