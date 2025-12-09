package com.miraflores.agenda.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miraflores.agenda.R
import com.miraflores.agenda.adapter.AppointmentsAdapter
import com.miraflores.agenda.db.AgendaDbHelper
import com.miraflores.agenda.model.Appointment
import com.miraflores.agenda.utils.ColorHelper
import com.miraflores.agenda.utils.NotificationHelper

class AppointmentsListActivity : AppCompatActivity() {

    private lateinit var dbHelper: AgendaDbHelper
    private lateinit var rootLayout: androidx.constraintlayout.widget.ConstraintLayout
    private lateinit var recycler: RecyclerView
    private lateinit var tvTitle: TextView

    private var filterService: String? = null
    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments_list)

        // --- PEDIR PERMISO DE NOTIFICACIÓN AL ENTRAR (ANDROID 13+) ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
        // -------------------------------------------------------------

        filterService = intent.getStringExtra("EXTRA_SERVICE")
        isAdmin = intent.getBooleanExtra("IS_ADMIN", false)

        rootLayout = findViewById(R.id.rootLayout)
        tvTitle = findViewById(R.id.title)
        ColorHelper.applyBackgroundColor(this, rootLayout)

        dbHelper = AgendaDbHelper(this)
        recycler = findViewById(R.id.recyclerAppointments)
        recycler.layoutManager = LinearLayoutManager(this)

        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        if (filterService != null) {
            tvTitle.text = "CITAS: ${filterService?.uppercase()}"
        } else {
            tvTitle.text = "HISTORIAL COMPLETO"
        }

        loadAppointments()
    }

    override fun onResume() {
        super.onResume()
        ColorHelper.applyBackgroundColor(this, rootLayout)
        loadAppointments()
    }

    private fun loadAppointments() {
        val list = if (filterService != null) {
            dbHelper.getAppointmentsByService(filterService!!)
        } else {
            dbHelper.getAllAppointments()
        }

        recycler.adapter = AppointmentsAdapter(list,
            onEditClick = { appointment ->
                if (isAdmin) {
                    confirmarCitaAdmin(appointment)
                } else {
                    // Editar usuario normal
                    val intent = Intent(this, AppointmentActivity::class.java)
                    intent.putExtra("EXTRA_ID", appointment.id)
                    startActivity(intent)
                }
            },
            onDeleteClick = { appointment ->
                if (isAdmin) {
                    rechazarCitaAdmin(appointment)
                } else {
                    showDeleteConfirmation(appointment.id)
                }
            }
        )

        if(list.isEmpty()){
            Toast.makeText(this, "No hay citas", Toast.LENGTH_SHORT).show()
        }
    }

    // --- ACCIÓN: CONFIRMAR (LANZA NOTIFICACIÓN) ---
    private fun confirmarCitaAdmin(appointment: Appointment) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Cita")
            .setMessage("¿Confirmar asistencia para ${appointment.clientName}?")
            .setPositiveButton("Sí, Confirmar") { _, _ ->

                // 1. Actualizar BD a CONFIRMADO
                dbHelper.updateStatus(appointment.id, "CONFIRMADO")

                // 2. LANZAR NOTIFICACIÓN AL SISTEMA
                NotificationHelper.showNotification(
                    this,
                    "Cita Confirmada ✅",
                    "Estimado/a ${appointment.clientName}, su cita de ${appointment.service} ha sido aprobada."
                )

                loadAppointments()
            }
            .setNeutralButton("Editar") { _, _ ->
                val intent = Intent(this, AppointmentActivity::class.java)
                intent.putExtra("EXTRA_ID", appointment.id)
                startActivity(intent)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // --- ACCIÓN: RECHAZAR (LANZA NOTIFICACIÓN) ---
    private fun rechazarCitaAdmin(appointment: Appointment) {
        AlertDialog.Builder(this)
            .setTitle("Rechazar Cita")
            .setMessage("¿Qué deseas hacer con esta cita?")
            .setPositiveButton("Rechazar (Notificar)") { _, _ ->

                dbHelper.updateStatus(appointment.id, "RECHAZADO")

                NotificationHelper.showNotification(
                    this,
                    "Cita Rechazada ❌",
                    "Hola ${appointment.clientName}, no pudimos agendar tu cita de ${appointment.service}. Contáctanos."
                )

                loadAppointments()
            }
            .setNegativeButton("Borrar Definitivamente") { _, _ ->
                dbHelper.deleteAppointment(appointment.id)
                loadAppointments()
            }
            .show()
    }

    private fun showDeleteConfirmation(id: Int) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Borrar del historial?")
            .setPositiveButton("Sí") { _, _ ->
                dbHelper.deleteAppointment(id)
                loadAppointments()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
