package com.example.mantenimientovehiculos.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mantenimientovehiculos.data.local.Vehicle
import com.example.mantenimientovehiculos.databinding.ActivityVehicleDetailBinding
import com.example.mantenimientovehiculos.viewmodel.VehicleViewModel

class VehicleDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleDetailBinding
    private val vehicleViewModel: VehicleViewModel by viewModels()

    private var currentVehicle: Vehicle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vehicleId = intent.getIntExtra("VEHICLE_ID", -1)

        if (vehicleId != -1) {
            vehicleViewModel.getVehicleById(vehicleId).observe(this) { vehicle ->
                if (vehicle != null) {
                    this.currentVehicle = vehicle

                    // --- INICIO DE LA MODIFICACIÓN ---
                    // Cargar datos en los widgets del nuevo layout

                    // Título principal
                    binding.textViewDetailTitle.text = "${vehicle.brand} ${vehicle.model}"
                    supportActionBar?.title = "${vehicle.brand} ${vehicle.model}"

                    // Tarjeta de Información General
                    binding.textViewDetailPlate.text = vehicle.plate
                    binding.textViewDetailYear.text = vehicle.year.toString()

                    // Tarjeta de Mantenimiento de Motor
                    binding.textViewEngineLastKm.text = String.format("%,d Km", vehicle.engineLastKm)
                    binding.textViewEngineNextKm.text = String.format("%,d Km", vehicle.engineNextKm)

                    // Tarjeta de Mantenimiento de Caja
                    binding.textViewTransmissionLastKm.text = String.format("%,d Km", vehicle.transmissionLastKm)
                    binding.textViewTransmissionNextKm.text = String.format("%,d Km", vehicle.transmissionNextKm)
                    // --- FIN DE LA MODIFICACIÓN ---

                    // Cargar imagen
                    if (!vehicle.imagePath.isNullOrEmpty()) {
                        binding.imageViewDetailPhoto.visibility = View.VISIBLE
                        binding.imageViewDetailPhoto.setImageURI(Uri.parse(vehicle.imagePath))
                    } else {
                        binding.imageViewDetailPhoto.visibility = View.GONE
                    }

                } else {
                    Toast.makeText(this, "Error: Vehículo no encontrado", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "Error: ID de vehículo inválido", Toast.LENGTH_LONG).show()
            finish()
        }

        // Listener para el botón de borrar
        binding.buttonDeleteVehicle.setOnClickListener {
            currentVehicle?.let { vehicleToDelete ->
                showDeleteConfirmationDialog(vehicleToDelete)
            }
        }

        // Listener para el botón de editar
        binding.buttonEditVehicle.setOnClickListener {
            currentVehicle?.let { vehicleToEdit ->
                val intent = Intent(this, AddVehicleActivity::class.java).apply {
                    putExtra("VEHICLE_ID", vehicleToEdit.id)
                }
                startActivity(intent)
            }
        }

        // Listener para el botón de sincronizar
        binding.buttonSyncVehicle.setOnClickListener {
            currentVehicle?.let { vehicleToSync ->
                Toast.makeText(this, "Sincronizando...", Toast.LENGTH_SHORT).show()
                vehicleViewModel.sendVehicle(vehicleToSync)
            }
        }
    }

    private fun showDeleteConfirmationDialog(vehicle: Vehicle) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar borrado")
            .setMessage("¿Estás seguro de que quieres borrar el vehículo ${vehicle.brand} ${vehicle.model}?")
            .setPositiveButton("Borrar") { _, _ ->
                vehicleViewModel.delete(vehicle)
                Toast.makeText(this, "Vehículo borrado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
