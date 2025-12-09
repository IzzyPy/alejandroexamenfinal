// Ruta: ui/LogActivity.kt
package com.example.mantenimientovehiculos.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mantenimientovehiculos.databinding.ActivityLogBinding
import com.example.mantenimientovehiculos.viewmodel.LogViewModel

class LogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogBinding
    // Usamos by viewModels() para obtener la instancia del LogViewModel
    private val logViewModel: LogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflamos el layout usando ViewBinding
        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ponemos un título a la ActionBar para que el usuario sepa dónde está
        supportActionBar?.title = "Historial de Eventos"

        // 1. Creamos una instancia de nuestro LogAdapter
        val adapter = LogAdapter()

        // 2. Configuramos el RecyclerView
        binding.recyclerViewLogs.adapter = adapter
        binding.recyclerViewLogs.layoutManager = LinearLayoutManager(this)

        // 3. Observamos el LiveData del ViewModel
        logViewModel.allLogs.observe(this, Observer { logs ->
            // Cuando la lista de logs cambia, se la pasamos al adaptador.
            // El ListAdapter se encargará de actualizar la UI de forma eficiente.
            logs?.let {
                adapter.submitList(it)
            }
        })
    }
}
