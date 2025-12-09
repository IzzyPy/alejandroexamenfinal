// Ruta: ui/VehicleAdapter.kt
package com.example.mantenimientovehiculos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimientovehiculos.data.local.Vehicle
import com.example.mantenimientovehiculos.databinding.ListItemVehicleBinding // Usaremos ViewBinding

class VehicleAdapter(private val onItemClicked: (Vehicle) -> Unit) :
    ListAdapter<Vehicle, VehicleAdapter.VehicleViewHolder>(DiffCallback) {

    // Usaremos ViewBinding también en el ViewHolder para más seguridad y limpieza.
    class VehicleViewHolder(private val binding: ListItemVehicleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vehicle: Vehicle) {
            binding.textViewBrandModel.text = "${vehicle.brand} ${vehicle.model}"
            binding.textViewPlate.text = vehicle.plate
            binding.textViewYear.text = vehicle.year.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding = ListItemVehicleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val currentVehicle = getItem(position)
        // Configuramos el clic aquí, llamando a la lambda que nos pasaron.
        holder.itemView.setOnClickListener {
            onItemClicked(currentVehicle)
        }
        holder.bind(currentVehicle)
    }

    // 'DiffCallback' calcula las diferencias entre la lista vieja y la nueva
    // para animar los cambios de forma eficiente.
    companion object DiffCallback : DiffUtil.ItemCallback<Vehicle>() {
        override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
            return oldItem == newItem
        }
    }
}
