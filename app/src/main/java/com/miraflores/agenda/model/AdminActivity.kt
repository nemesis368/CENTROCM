package com.miraflores.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Referencias a los botones de especialidades
        val btnOdontologia = findViewById<Button>(R.id.btnOdontologia)
        val btnPsicologia = findViewById<Button>(R.id.btnPsicologia)
        val btnConsultaGeneral = findViewById<Button>(R.id.btnConsultaGeneral)

        // Referencia al nuevo botón de salir
        val btnSalir = findViewById<TextView>(R.id.btnCerrarSesionAdmin)

        // --- FUNCIONES DE LOS BOTONES (Por ahora solo mensajes) ---
        btnOdontologia.setOnClickListener {
            Toast.makeText(this, "Gestionar Odontología", Toast.LENGTH_SHORT).show()
        }

        btnPsicologia.setOnClickListener {
            Toast.makeText(this, "Gestionar Psicología", Toast.LENGTH_SHORT).show()
        }

        btnConsultaGeneral.setOnClickListener {
            Toast.makeText(this, "Gestionar Consulta General", Toast.LENGTH_SHORT).show()
        }

        // --- LÓGICA DE CERRAR SESIÓN ---
        btnSalir.setOnClickListener {
            // 1. Te lleva de regreso al Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // 2. Destruye la pantalla de Admin para que no puedas volver con "Atrás"
            finish()
        }
    }
}