package com.miraflores.agenda

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miraflores.agenda.adapter.AgendaAdapter
import com.miraflores.agenda.db.CitaDAO

class HistorialodoActivity : AppCompatActivity() {

    private lateinit var citaDAO: CitaDAO
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AgendaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historialodo)

        recyclerView = findViewById(R.id.rvHistorial)
        recyclerView.layoutManager = LinearLayoutManager(this)

        citaDAO = CitaDAO(this)

        cargarHistorial()
    }

    private fun cargarHistorial() {
        try {
            val listaHistorial = citaDAO.obtenerTodos()

            if (listaHistorial.isEmpty()) {
                Toast.makeText(this, "No hay citas registradas aún", Toast.LENGTH_SHORT).show()
            }

            adapter = AgendaAdapter(listaHistorial)
            recyclerView.adapter = adapter

        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}