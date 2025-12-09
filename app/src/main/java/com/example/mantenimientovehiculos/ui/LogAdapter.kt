package com.example.mantenimientovehiculos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mantenimientovehiculos.data.local.LogEntry
import com.example.mantenimientovehiculos.databinding.ListItemLogBinding

class LogAdapter : ListAdapter<LogEntry, LogAdapter.LogViewHolder>(LogDiffCallback()) {

    /**
     * ViewHolder para cada item de log.
     * Mantiene las referencias a las vistas (TextViews) del layout 'list_item_log.xml'.
     */
    class LogViewHolder(private val binding: ListItemLogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(logEntry: LogEntry) {
            binding.textViewLogMessage.text = logEntry.message
            // Usamos la función de ayuda que creamos en la entidad LogEntry
            binding.textViewLogTimestamp.text = logEntry.getFormattedTimestamp()
        }
    }

    /**
     * Se llama cuando el RecyclerView necesita crear un nuevo ViewHolder.
     * Infla el layout 'list_item_log.xml' y crea una instancia de LogViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = ListItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    /**
     * Se llama cuando el RecyclerView necesita mostrar los datos en un ViewHolder específico.
     * Obtiene el LogEntry en la posición actual y llama a la función bind() del ViewHolder.
     */
    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val logEntry = getItem(position)
        holder.bind(logEntry)
    }

    /**
     * DiffUtil.ItemCallback para que ListAdapter sepa cómo calcular las diferencias
     * entre la lista vieja y la nueva, y así animar los cambios de forma eficiente.
     */
    class LogDiffCallback : DiffUtil.ItemCallback<LogEntry>() {
        override fun areItemsTheSame(oldItem: LogEntry, newItem: LogEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LogEntry, newItem: LogEntry): Boolean {
            return oldItem == newItem
        }
    }
}
