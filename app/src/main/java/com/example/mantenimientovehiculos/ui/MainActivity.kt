// Ruta: ui/MainActivity.kt
package com.example.mantenimientovehiculos.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView // Importa el SearchView correcto
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mantenimientovehiculos.R // Importación para acceder a los recursos como el menú
import com.example.mantenimientovehiculos.databinding.ActivityMainBinding
import com.example.mantenimientovehiculos.viewmodel.VehicleViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vehicleViewModel: VehicleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- INICIO DE LA MODIFICACIÓN ---
        // Establecemos nuestra ToolBar personalizada (del XML) como la ActionBar oficial de la actividad.
        setSupportActionBar(binding.toolbarMain)
        // Ponemos un título en la nueva ToolBar.
        supportActionBar?.title = "Historial de Eventos"
        // --- FIN DE LA MODIFICACIÓN ---

        val adapter = VehicleAdapter { vehicle ->
            val intent = Intent(this, VehicleDetailActivity::class.java).apply {
                putExtra("VEHICLE_ID", vehicle.id)
            }
            startActivity(intent)
        }

        // Asegúrate de que el ID del RecyclerView en tu layout sea 'recycler_view_vehicles'
        binding.recyclerViewVehicles.adapter = adapter
        binding.recyclerViewVehicles.layoutManager = LinearLayoutManager(this)

        // El observer no cambia. Reaccionará a la lista completa o a la lista filtrada.
        vehicleViewModel.allVehicles.observe(this, Observer { vehicles ->
            vehicles?.let { adapter.submitList(it) }
        })

        binding.fabAddVehicle.setOnClickListener {
            val intent = Intent(this, AddVehicleActivity::class.java)
            startActivity(intent)
        }

        // Lógica del buscador para filtrar en tiempo real.
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                vehicleViewModel.search(newText.orEmpty())
                return true
            }
        })
    }

    // Se llama cuando la actividad necesita crear el menú de opciones de la ActionBar.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Se llama cuando el usuario selecciona una opción del menú.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Si el item seleccionado es el de ver los logs...
            R.id.action_view_logs -> {
                // Creamos un Intent para abrir LogActivity.
                val intent = Intent(this, LogActivity::class.java)
                startActivity(intent)
                true // Indicamos que hemos manejado el clic.
            }
            // Si no es el nuestro, dejamos que el sistema lo maneje.
            else -> super.onOptionsItemSelected(item)
        }
    }
}
