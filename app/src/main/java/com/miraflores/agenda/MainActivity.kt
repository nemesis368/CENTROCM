package com.miraflores.agenda

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Botón 1: Agenda
        val btnAgenda = findViewById<CardView>(R.id.btnAgenda)
        btnAgenda.setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }

        // Botón 2: IA Gemini
        val btnIA = findViewById<CardView>(R.id.btnIA)
        btnIA.setOnClickListener {
            startActivity(Intent(this, GeminiChatActivity::class.java))
        }

        // Botón 3: Mapa
        val btnMapa = findViewById<CardView>(R.id.btnMapa)
        btnMapa.setOnClickListener {
            startActivity(Intent(this, MapaActivity::class.java))
        }

        // Cerrar sesión
        val btnCerrar = findViewById<TextView>(R.id.btnCerrarSesion)
        btnCerrar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
