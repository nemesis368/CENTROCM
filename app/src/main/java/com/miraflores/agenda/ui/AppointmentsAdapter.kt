package com.miraflores.agenda.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miraflores.agenda.R
import com.miraflores.agenda.model.Appointment

class AppointmentsAdapter(
    private val list: List<Appointment>,
    private val onEditClick: (Appointment) -> Unit,
    private val onDeleteClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AppointmentsAdapter.ApptViewHolder>() {

    class ApptViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.ivIconService)
        val statusIcon: ImageView = view.findViewById(R.id.ivStatusIcon) // Nuevo icono
        val tvService: TextView = view.findViewById(R.id.tvService)
        val tvName: TextView = view.findViewById(R.id.tvClientName)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val btnEdit: ImageView = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageView = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApptViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return ApptViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApptViewHolder, position: Int) {
        val item = list[position]

        holder.tvService.text = item.service
        holder.tvName.text = item.clientName
        holder.tvDate.text = item.date
        holder.tvTime.text = item.time
        holder.tvDetails.text = "Edad: ${item.age} | Tel: ${item.phone}\n${item.notes}"

        // --- LÓGICA DE ESTADO (Palomita / X) ---
        holder.statusIcon.visibility = View.VISIBLE
        when (item.status) {
            "CONFIRMADO" -> {
                holder.statusIcon.setImageResource(android.R.drawable.checkbox_on_background) // Palomita
                holder.statusIcon.setColorFilter(android.graphics.Color.GREEN)
            }
            "RECHAZADO" -> {
                holder.statusIcon.setImageResource(android.R.drawable.ic_delete) // X o Tache
                holder.statusIcon.setColorFilter(android.graphics.Color.RED)
            }
            else -> {
                // Pendiente (Reloj o invisible)
                holder.statusIcon.setImageResource(android.R.drawable.ic_menu_recent_history)
                holder.statusIcon.setColorFilter(android.graphics.Color.GRAY)
            }
        }

        // Icono de servicio
        when (item.service) {
            "Odontología" -> holder.icon.setImageResource(R.drawable.ic_calendar)
            "Psicología" -> holder.icon.setImageResource(R.drawable.ic_calendar)
            else -> holder.icon.setImageResource(R.drawable.ic_calendar)
        }

        holder.btnEdit.setOnClickListener { onEditClick(item) }
        holder.btnDelete.setOnClickListener { onDeleteClick(item) }
    }

    override fun getItemCount() = list.size
}
