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

        // --- BOTONES DEL MENÚ ---

        // Botón 1: Agenda
        findViewById<Button>(R.id.btnAgenda).setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }

        // Botón 2: Asistente IA
        findViewById<Button>(R.id.btnIA).setOnClickListener {
            startActivity(Intent(this, GeminiChatActivity::class.java))
        }

        // Botón 3: Ubicación / Mapa
        findViewById<Button>(R.id.btnMapa).setOnClickListener {
            startActivity(Intent(this, MapaActivity::class.java))
        }

        // --- LÓGICA DE CERRAR SESIÓN ---
        // Buscamos el texto que agregamos al final del diseño
        val btnCerrarSesion = findViewById<TextView>(R.id.btnCerrarSesion)

        btnCerrarSesion.setOnClickListener {
            // 1. Creamos el viaje de regreso al Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // 2. Destruimos esta pantalla (Menú) para que no se pueda volver con "Atrás"
            finish()
        }
    }
}
