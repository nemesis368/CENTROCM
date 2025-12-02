package com.miraflores.agenda

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.miraflores.agenda.adapter.AgendaAdapter
import com.miraflores.agenda.db.CitaDAO
import com.miraflores.agenda.model.Cita
import java.text.SimpleDateFormat
import java.util.*

class AgendaActivity : AppCompatActivity() {
    lateinit var dao: CitaDAO
    lateinit var adapter: AgendaAdapter
    private var fechaStr: String = ""
    private var horaStr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)
        dao = CitaDAO(this)

        val spinner = findViewById<AutoCompleteTextView>(R.id.spinnerServicio)
        val etPac = findViewById<EditText>(R.id.etPacienteNew)
        val calendar = findViewById<CalendarView>(R.id.calendarView)
        val chips = findViewById<ChipGroup>(R.id.chipGroupHoras)
        val btn = findViewById<Button>(R.id.btnConfirmarCita)
        val rv = findViewById<RecyclerView>(R.id.rvCitas)

        // Configurar RecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        adapter = AgendaAdapter(listOf())
        rv.adapter = adapter
        actualizarLista()

        // Configurar Spinner
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayOf("Dental", "Psicología", "General", "Terapia"))
        spinner.setAdapter(adapterSpinner)

        // Fecha
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        fechaStr = sdf.format(Date())
        calendar.setOnDateChangeListener { _, y, m, d ->
            val cal = Calendar.getInstance()
            cal.set(y, m, d)
            fechaStr = sdf.format(cal.time)
        }

        // Hora
        chips.setOnCheckedStateChangeListener { group, ids ->
            if(ids.isNotEmpty()) horaStr = group.findViewById<Chip>(ids[0]).text.toString()
        }

        btn.setOnClickListener {
            if(etPac.text.isNotEmpty() && horaStr.isNotEmpty()){
                dao.insertar(Cita(servicio=spinner.text.toString(), paciente=etPac.text.toString(), fecha="$fechaStr - $horaStr"))
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show()
                actualizarLista()
                etPac.setText("")
            } else {
                Toast.makeText(this, "Faltan datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarLista(){
        adapter.actualizarLista(dao.obtenerTodos())
    }
}
