package com.miraflores.agenda.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.miraflores.agenda.R
import com.miraflores.agenda.model.Cita


class AgendaAdapter(private var lista: List<Cita>) : RecyclerView.Adapter<AgendaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvServicio: TextView = view.findViewById(R.id.tvItemServicio)
        val tvPaciente: TextView = view.findViewById(R.id.tvItemPaciente)
        val tvFecha: TextView = view.findViewById(R.id.tvItemFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Necesitamos crear el layout 'item_cita.xml' (ver abajo en layouts)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.tvServicio.text = item.servicio
        holder.tvPaciente.text = item.paciente
        holder.tvFecha.text = item.fecha
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<Cita>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
