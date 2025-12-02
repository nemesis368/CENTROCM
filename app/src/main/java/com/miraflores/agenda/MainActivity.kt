package com.miraflores.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // 1. Intentamos cargar el diseño. Si falta un color o imagen, fallará aquí.
            setContentView(R.layout.activity_main)

            // 2. Si el diseño carga bien, configuramos los botones
            configurarBotones()

        } catch (e: Exception) {
            // 3. SI FALLA: En lugar de cerrarse, te muestra el error en una ventana
            mostrarError(e)
        }
    }

    private fun configurarBotones() {
        findViewById<Button>(R.id.btnAgenda).setOnClickListener {
            startActivity(Intent(this, AgendaActivity::class.java))
        }

        findViewById<Button>(R.id.btnIA).setOnClickListener {
            startActivity(Intent(this, GeminiChatActivity::class.java))
        }

        findViewById<Button>(R.id.btnMapa).setOnClickListener {
            startActivity(Intent(this, MapaActivity::class.java))
        }
    }

    private fun mostrarError(e: Exception) {
        val mensaje = e.cause?.message ?: e.message ?: "Error desconocido"

        AlertDialog.Builder(this)
            .setTitle("⚠️ Error de Diseño")
            .setMessage("La pantalla no pudo cargarse.\n\nERROR:\n$mensaje\n\nPOSIBLE SOLUCIÓN:\nVerifica que existan 'colors.xml' y 'bg_boton_gradiente.xml'")
            .setPositiveButton("OK", null)
            .show()
    }
}