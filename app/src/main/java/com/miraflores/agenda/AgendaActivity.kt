package com.miraflores.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.miraflores.agenda.db.CitaDAO
import com.miraflores.agenda.model.Cita
import java.text.SimpleDateFormat
import java.util.*

class AgendaActivity : AppCompatActivity() {
    lateinit var dao: CitaDAO
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
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmarCita)
        val btnVerHistorial = findViewById<Button>(R.id.btnVerHistorial)

        // Configurar Spinner
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayOf("Dental", "Psicología", "General", "Terapia"))
        spinner.setAdapter(adapterSpinner)

        // Fecha inicial
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        fechaStr = sdf.format(Date())

        calendar.setOnDateChangeListener { _, y, m, d ->
            val cal = Calendar.getInstance()
            cal.set(y, m, d)
            fechaStr = sdf.format(cal.time)
        }

        // Selección de Hora
        chips.setOnCheckedStateChangeListener { group, ids ->
            if(ids.isNotEmpty()) {
                val chip = group.findViewById<Chip>(ids[0])
                horaStr = chip.text.toString()
            }
        }

        // GUARDAR CITA
        btnConfirmar.setOnClickListener {
            if(etPac.text.isNotEmpty() && horaStr.isNotEmpty()){

                // Creamos la cita (el ID se pone solo en 0)
                val nuevaCita = Cita(
                    servicio = spinner.text.toString(),
                    paciente = etPac.text.toString(),
                    fecha = "$fechaStr - $horaStr"
                )

                dao.insertar(nuevaCita)
                Toast.makeText(this, "Cita guardada correctamente", Toast.LENGTH_SHORT).show()

                etPac.setText("")
                chips.clearCheck()
                horaStr = ""
            } else {
                Toast.makeText(this, "Faltan datos (Paciente u Hora)", Toast.LENGTH_SHORT).show()
            }
        }

        // VER HISTORIAL
        btnVerHistorial.setOnClickListener {
            val intent = Intent(this, HistorialodoActivity::class.java)
            startActivity(intent)
        }
    }
}
