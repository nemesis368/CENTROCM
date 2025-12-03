package com.miraflores.agenda

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.miraflores.agenda.db.UsuarioDAO

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val dao = UsuarioDAO(this)

        // Conectamos con los IDs que pusimos en el paso 2
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etUsuario = findViewById<EditText>(R.id.etUsuarioRegistro)
        val etPass = findViewById<EditText>(R.id.etPassRegistro)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val usuario = etUsuario.text.toString().trim()
            val pass = etPass.text.toString().trim()

            // Verificamos que no haya nada vacío
            if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty() || usuario.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Falta llenar algún dato", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                // Guardamos en la base de datos
                val exito = dao.registrar(nombre, telefono, correo, usuario, pass)

                if (exito) {
                    Toast.makeText(this, "¡Registro Exitoso!", Toast.LENGTH_LONG).show()
                    finish() // Cierra registro y vuelve al login
                } else {
                    Toast.makeText(this, "Ese usuario ya existe, prueba otro", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
