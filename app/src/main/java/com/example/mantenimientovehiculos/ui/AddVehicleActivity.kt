// Ruta: ui/AddVehicleActivity.kt
package com.example.mantenimientovehiculos.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.mantenimientovehiculos.data.local.Vehicle
import com.example.mantenimientovehiculos.databinding.ActivityAddVehicleBinding
import com.example.mantenimientovehiculos.viewmodel.VehicleViewModel
import kotlinx.coroutines.launch
import java.io.File

class AddVehicleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVehicleBinding
    private val vehicleViewModel: VehicleViewModel by viewModels()

    private var latestTmpUri: Uri? = null
    private var imagePath: String? = null

    private var vehicleId: Int = -1
    private var isEditMode = false

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                binding.imageViewVehiclePhoto.setImageURI(uri)
                imagePath = uri.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vehicleId = intent.getIntExtra("VEHICLE_ID", -1)
        isEditMode = vehicleId != -1

        if (isEditMode) {
            supportActionBar?.title = "Editar Vehículo"
            // Seguimos usando LiveData aquí, que es su uso correcto: observar datos para la UI.
            loadVehicleData()
        } else {
            supportActionBar?.title = "Añadir Vehículo"
        }

        binding.imageViewVehiclePhoto.setOnClickListener {
            takeImage()
        }

        binding.buttonSave.setOnClickListener {
            saveVehicle()
        }
    }

    private fun loadVehicleData() {
        vehicleViewModel.getVehicleById(vehicleId).observe(this) { vehicle ->
            vehicle?.let {
                binding.editTextBrand.setText(it.brand)
                binding.editTextModel.setText(it.model)
                binding.editTextPlate.setText(it.plate)
                binding.editTextYear.setText(it.year.toString())
                binding.editTextEngineLastKm.setText(it.engineLastKm.toString())
                binding.editTextEngineNextKm.setText(it.engineNextKm.toString())
                binding.editTextTransmissionLastKm.setText(it.transmissionLastKm.toString())
                binding.editTextTransmissionNextKm.setText(it.transmissionNextKm.toString())

                if (!it.imagePath.isNullOrEmpty()) {
                    this.imagePath = it.imagePath
                    binding.imageViewVehiclePhoto.setImageURI(Uri.parse(imagePath))
                }
            }
        }
    }

    private fun takeImage() {
        getTmpFileUri().let { uri ->
            latestTmpUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(applicationContext, "${packageName}.provider", tmpFile)
    }

    private fun saveVehicle() {
        val brand = binding.editTextBrand.text.toString().trim()
        val model = binding.editTextModel.text.toString().trim()
        val plate = binding.editTextPlate.text.toString().trim()
        val yearString = binding.editTextYear.text.toString().trim()
        val engineLastKmString = binding.editTextEngineLastKm.text.toString().trim()
        val engineNextKmString = binding.editTextEngineNextKm.text.toString().trim()
        val transmissionLastKmString = binding.editTextTransmissionLastKm.text.toString().trim()
        val transmissionNextKmString = binding.editTextTransmissionNextKm.text.toString().trim()

        if (brand.isEmpty() || model.isEmpty() || plate.isEmpty() || yearString.isEmpty() ||
            engineLastKmString.isEmpty() || engineNextKmString.isEmpty() ||
            transmissionLastKmString.isEmpty() || transmissionNextKmString.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val newVehicleData = Vehicle(
            id = if (isEditMode) vehicleId else 0,
            brand = brand, model = model, plate = plate,
            year = yearString.toInt(), imagePath = this.imagePath,
            engineLastKm = engineLastKmString.toInt(), engineNextKm = engineNextKmString.toInt(),
            transmissionLastKm = transmissionLastKmString.toInt(), transmissionNextKm = transmissionNextKmString.toInt()
        )

        if (isEditMode) {
            // --- INICIO DE LA SOLUCIÓN ---
            // Usamos una corutina para obtener el 'oldVehicle' una sola vez y evitar el bug.
            lifecycleScope.launch {
                val oldVehicle = vehicleViewModel.getVehicleOnce(vehicleId)
                if (oldVehicle != null) {
                    vehicleViewModel.update(oldVehicle, newVehicleData)
                    // Usamos this@AddVehicleActivity para especificar el contexto correcto dentro de la corutina
                    Toast.makeText(this@AddVehicleActivity, "Vehículo actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            // --- FIN DE LA SOLUCIÓN ---
        } else {
            // La lógica de inserción no cambia
            vehicleViewModel.insert(newVehicleData)
            Toast.makeText(this, "Vehículo guardado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
