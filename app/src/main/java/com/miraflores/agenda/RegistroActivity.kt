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

        // --- AQUÍ ESTABA EL ERROR: AHORA USAMOS TUS IDs CORRECTOS DEL XML ---
        val etNombre = findViewById<EditText>(R.id.etNombreReg)
        val etTelefono = findViewById<EditText>(R.id.etTelefonoReg)
        val etCorreo = findViewById<EditText>(R.id.etCorreoReg)
        val etUsuario = findViewById<EditText>(R.id.etUsuarioReg)
        val etPass = findViewById<EditText>(R.id.etPassReg)

        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        // --- LÓGICA DEL BOTÓN ---
        btnRegistrar.setOnClickListener {
            // 1. Obtenemos el texto de los campos
            val nombre = etNombre.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val usuario = etUsuario.text.toString().trim()
            val pass = etPass.text.toString().trim()

            // 2. Validamos que no estén vacíos
            if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty() || usuario.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Falta llenar algún dato", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                // 3. Guardamos en la Base de Datos
                val guardado = dao.registrar(nombre, telefono, correo, usuario, pass)

                if (guardado) {
                    Toast.makeText(this, "¡Cuenta creada exitosamente!", Toast.LENGTH_LONG).show()

                    // 4. Cerramos esta pantalla para volver al Login automáticamente
                    finish()
                } else {
                    Toast.makeText(this, "Error: El usuario ya existe o hubo un fallo.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error crítico: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}