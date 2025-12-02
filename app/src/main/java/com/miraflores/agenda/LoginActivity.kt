package com.miraflores.agenda

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.miraflores.agenda.db.UsuarioDAO

class LoginActivity : AppCompatActivity() {

    private var esModoCliente = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializamos DAO con protección por si falla la BD al arrancar
        val dao = try {
            UsuarioDAO(this)
        } catch (e: Exception) {
            null // Si falla la base de datos, dejamos el dao como nulo
        }

        val etUser = findViewById<EditText>(R.id.etUser)
        val etPass = findViewById<EditText>(R.id.etPass)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegistrar = findViewById<TextView>(R.id.tvRegistrar)
        val tvLoginHeader = findViewById<TextView>(R.id.tvLoginHeader)
        val tvTabCliente = findViewById<TextView>(R.id.tvTabCliente)
        val tvTabAdmin = findViewById<TextView>(R.id.tvTabAdmin)

        // --- LÓGICA VISUAL ---
        fun actualizarModo(cliente: Boolean) {
            esModoCliente = cliente
            if (esModoCliente) {
                tvTabCliente.setTextColor(Color.WHITE)
                tvTabCliente.setTypeface(null, Typeface.BOLD)
                tvTabAdmin.setTextColor(Color.parseColor("#DDDDDD"))
                tvTabAdmin.setTypeface(null, Typeface.NORMAL)
                tvLoginHeader.text = "Acceso Clientes"
                etUser.hint = "Usuario"
                tvRegistrar.visibility = android.view.View.VISIBLE
            } else {
                tvTabAdmin.setTextColor(Color.WHITE)
                tvTabAdmin.setTypeface(null, Typeface.BOLD)
                tvTabCliente.setTextColor(Color.parseColor("#DDDDDD"))
                tvTabCliente.setTypeface(null, Typeface.NORMAL)
                tvLoginHeader.text = "Acceso Administrativo"
                etUser.hint = "ID Admin"
                tvRegistrar.visibility = android.view.View.GONE
            }
            etUser.text.clear()
            etPass.text.clear()
        }

        tvTabCliente.setOnClickListener { actualizarModo(true) }
        tvTabAdmin.setOnClickListener { actualizarModo(false) }

        // --- BOTÓN ACCEDER CON SEGURIDAD ---
        btnLogin.setOnClickListener {
            try {
                val u = etUser.text.toString().trim()
                val p = etPass.text.toString().trim()

                if (esModoCliente) {
                    // 1. PRIMERO verificamos la cuenta de respaldo (Bruno)
                    // Esto evita que un error en la base de datos bloquee el acceso
                    if (u == "Bruno" && p == "123") {
                        ingresarAlMenu(u)
                        return@setOnClickListener
                    }

                    // 2. LUEGO intentamos con la base de datos (si existe)
                    if (dao != null) {
                        if (dao.login(u, p)) {
                            ingresarAlMenu(u)
                        } else {
                            mostrarAlerta("Datos incorrectos", "Usuario no encontrado o contraseña inválida.")
                        }
                    } else {
                        mostrarAlerta("Error de Base de Datos", "No se pudo conectar con la base de datos. Usa el usuario 'Bruno' para entrar.")
                    }

                } else {
                    // MODO ADMIN
                    if (u == "admin" && p == "123") {
                        val intent = Intent(this, AdminActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        mostrarAlerta("Acceso Denegado", "Credenciales de administrador incorrectas.")
                    }
                }

            } catch (e: Exception) {
                // SI OCURRE UN ERROR: Lo mostramos en lugar de cerrar la app
                mostrarAlerta("Error Crítico en Login", e.message ?: "Error desconocido")
                e.printStackTrace()
            }
        }

        tvRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun ingresarAlMenu(usuario: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USUARIO_ACTUAL", usuario)
        startActivity(intent)
        finish()
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("OK", null)
            .show()
    }
}