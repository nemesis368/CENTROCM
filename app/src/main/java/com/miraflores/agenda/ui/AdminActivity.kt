package com.miraflores.agenda.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.miraflores.agenda.databinding.ActivityAdminBinding
import com.miraflores.agenda.utils.ColorHelper

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aplicar el color de fondo guardado en Ajustes
        ColorHelper.applyBackgroundColor(this, binding.rootLayout)

        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        ColorHelper.applyBackgroundColor(this, binding.rootLayout)
    }

    private fun setupListeners() {

        // 1. ODONTOLOGÍA
        binding.btnOdontologia.setOnClickListener {
            openServiceList("Odontología")
        }

        // 2. PSICOLOGÍA
        binding.btnPsicologia.setOnClickListener {
            openServiceList("Psicología")
        }

        // 3. CONSULTA GENERAL
        binding.btnConsultaGeneral.setOnClickListener {
            openServiceList("Consulta General")
        }

        // CERRAR SESIÓN
        binding.btnCerrarSesionAdmin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun openServiceList(serviceName: String) {
        val intent = Intent(this, AppointmentsListActivity::class.java)
        // Enviamos el filtro y la marca de que es Admin
        intent.putExtra("EXTRA_SERVICE", serviceName)
        intent.putExtra("IS_ADMIN", true)
        startActivity(intent)

        Toast.makeText(this, "Filtrando: $serviceName", Toast.LENGTH_SHORT).show()
    }
}