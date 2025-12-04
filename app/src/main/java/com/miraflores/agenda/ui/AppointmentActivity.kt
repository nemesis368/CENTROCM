package com.miraflores.agenda.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide // <--- IMPORTANTE: Importar Glide
import com.miraflores.agenda.R
import com.miraflores.agenda.databinding.ActivityAppointmentBinding
import com.miraflores.agenda.db.AgendaDbHelper
import com.miraflores.agenda.model.Appointment
import com.miraflores.agenda.utils.ColorHelper
import java.util.Calendar

class AppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBinding
    private lateinit var dbHelper: AgendaDbHelper

    private var isEditMode = false
    private var appointmentId = -1
    private var isDateValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Aplicar color de fondo
        ColorHelper.applyBackgroundColor(this, binding.rootLayout)

        // 2. Cargar el GIF (ESTO FALTABA)
        try {
            Glide.with(this)
                .asGif()
                .load(R.drawable.dos) // Asegúrate que tu gif se llame 'dos'
                .into(binding.ivHeaderGif)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        dbHelper = AgendaDbHelper(this)

        setupSpinner()

        // Verificar si venimos a EDITAR
        if (intent.hasExtra("EXTRA_ID")) {
            isEditMode = true
            appointmentId = intent.getIntExtra("EXTRA_ID", -1)
            loadAppointmentData(appointmentId)
        }

        setupListeners()
    }

    private fun loadAppointmentData(id: Int) {
        val cita = dbHelper.getAppointmentById(id)
        if (cita != null) {
            binding.titleAppointment.text = "EDITAR CITA"
            binding.buttonSaveAppointment.text = "ACTUALIZAR CITA"

            // Llenar campos
            binding.inputAppointmentClientName.setText(cita.clientName)
            binding.inputAppointmentAge.setText(cita.age)
            binding.inputAppointmentPhone.setText(cita.phone)
            binding.inputAppointmentDate.setText(cita.date)
            binding.inputAppointmentTime.setText(cita.time)
            binding.inputAppointmentNotes.setText(cita.notes)

            // Seleccionar Spinner
            val adapter = binding.spinnerService.adapter as ArrayAdapter<String>
            val position = adapter.getPosition(cita.service)
            binding.spinnerService.setSelection(position)

            isDateValid = true // Asumimos que la fecha cargada es válida
        }
    }

    override fun onResume() {
        super.onResume()
        ColorHelper.applyBackgroundColor(this, binding.rootLayout)
    }

    private fun setupSpinner() {
        val opciones = arrayOf("Consulta General", "Odontología", "Psicología")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        binding.spinnerService.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        // Fecha
        binding.inputAppointmentDate.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                val selectedCal = Calendar.getInstance()
                selectedCal.set(year, month, day)
                val dayOfWeek = selectedCal.get(Calendar.DAY_OF_WEEK)

                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    binding.inputAppointmentDate.setTextColor(Color.YELLOW)
                    binding.inputAppointmentDate.setText("Día Inhábil")
                    Toast.makeText(this, "Fines de semana inhábiles", Toast.LENGTH_LONG).show()
                    isDateValid = false
                } else {
                    val formattedDate = "$day/${month + 1}/$year"
                    binding.inputAppointmentDate.setTextColor(Color.GREEN)
                    binding.inputAppointmentDate.setText(formattedDate)
                    isDateValid = true
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).apply {
                datePicker.minDate = System.currentTimeMillis() - 1000
            }.show()
        }

        // Hora
        binding.inputAppointmentTime.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(this, { _, h, m ->
                if (h < 9 || h > 18) {
                    Toast.makeText(this, "Horario: 09:00 - 18:00", Toast.LENGTH_SHORT).show()
                } else {
                    binding.inputAppointmentTime.setText(String.format("%02d:%02d", h, m))
                }
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show()
        }

        // Botón Guardar / Actualizar
        binding.buttonSaveAppointment.setOnClickListener {
            val client = binding.inputAppointmentClientName.text.toString().trim()
            val phone = binding.inputAppointmentPhone.text.toString().trim()
            val age = binding.inputAppointmentAge.text.toString().trim()
            val service = binding.spinnerService.selectedItem.toString()
            val date = binding.inputAppointmentDate.text.toString().trim()
            val time = binding.inputAppointmentTime.text.toString().trim()
            val notes = binding.inputAppointmentNotes.text.toString().trim()

            if (client.isEmpty() || time.isEmpty() || phone.isEmpty() || !isDateValid) {
                Toast.makeText(this, "Verifica los datos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cita = Appointment(
                id = if (isEditMode) appointmentId else -1,
                clientName = client,
                phone = phone,
                age = age,
                service = service,
                date = date,
                time = time,
                notes = notes
            )

            if (isEditMode) {
                // ACTUALIZAR
                dbHelper.updateAppointment(cita)
                Toast.makeText(this, "Cita actualizada", Toast.LENGTH_SHORT).show()
            } else {
                // INSERTAR NUEVA
                dbHelper.insertAppointment(cita)
                Toast.makeText(this, "Cita creada", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}