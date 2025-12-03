package com.miraflores.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Botón 1: Agenda
        val btnAgenda = findViewById<Button>(R.id.btnAgenda)
        btnAgenda.setOnClickListener {
            val intent = Intent(this, AgendaActivity::class.java)
            startActivity(intent)
        }

        // Botón 2: Asistente IA
        val btnIA = findViewById<Button>(R.id.btnIA)
        btnIA.setOnClickListener {
            val intent = Intent(this, GeminiChatActivity::class.java)
            startActivity(intent)
        }

        // Botón 3: Ubicación / Mapa
        val btnMapa = findViewById<Button>(R.id.btnMapa)
        btnMapa.setOnClickListener {
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
        }

        // Botón Cerrar Sesión (Es un TextView en el XML)
        val btnCerrar = findViewById<TextView>(R.id.btnCerrarSesion)
        btnCerrar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}