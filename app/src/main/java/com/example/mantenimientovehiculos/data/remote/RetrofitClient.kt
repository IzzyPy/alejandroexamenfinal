// Ruta: data/remote/RetrofitClient.kt
package com.example.mantenimientovehiculos.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // --- CAMBIO CLAVE ---
    // Al usar @Url en nuestras llamadas, la baseUrl se vuelve menos importante.
    // Ponemos una URL base válida pero genérica. La URL real se definirá en el Repository.
    private const val BASE_URL = "https://placeholder-url.com/"

    // El interceptor de logs es nuestro mejor amigo para depurar.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Creamos la instancia de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // Retrofit necesita una baseUrl para construirse, aunque luego no la usemos.
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Creamos una instancia de nuestra interfaz de servicio para poder usarla
    val apiService: VehicleApiService by lazy {
        retrofit.create(VehicleApiService::class.java)
    }
}

