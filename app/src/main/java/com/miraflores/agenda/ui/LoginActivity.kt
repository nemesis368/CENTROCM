package com.miraflores.agenda.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.miraflores.agenda.R
import com.miraflores.agenda.databinding.ActivityLoginBinding
import com.miraflores.agenda.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var isAdminMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar si ya hay sesión activa
        if (SessionManager.isLoggedIn(this)) {
            goToHome()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar solo Firebase Auth (Correo/Pass)
        auth = FirebaseAuth.getInstance()

        // Cargar GIF
        try {
            Glide.with(this).load(R.drawable.loginn).into(binding.ivLogoGif)
        } catch (e: Exception) { }

        setupUI()
    }

    private fun setupUI() {
        // Toggles Cliente / Admin
        binding.tvTabCliente.setOnClickListener {
            isAdminMode = false
            updateUIMode()
        }
        binding.tvTabAdmin.setOnClickListener {
            isAdminMode = true
            updateUIMode()
        }

        // BOTÓN ACCEDER
        binding.btnLogin.setOnClickListener {
            val user = binding.etUser.text.toString().trim()
            val pass = binding.etPass.text.toString().trim()

            if (isAdminMode) {
                // MODO ADMIN
                if (user == "admin" && pass == "123") {
                    Toast.makeText(this, "Bienvenido Administrador", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AdminActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Credenciales Admin Incorrectas", Toast.LENGTH_SHORT).show()
                }
            } else {
                // MODO CLIENTE (FIREBASE AUTH)
                if (user.isNotEmpty() && pass.isNotEmpty()) {
                    loginFirebase(user, pass)
                } else {
                    Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Ir a Registro
        binding.tvRegistrar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun updateUIMode() {
        binding.etUser.text?.clear()
        binding.etPass.text?.clear()

        if (isAdminMode) {
            // Estilos para Admin
            binding.tvTabAdmin.setTextColor(android.graphics.Color.WHITE)
            binding.tvTabCliente.setTextColor(android.graphics.Color.GRAY)
            binding.tvRegistrar.visibility = View.GONE // Admin no se registra aquí
            binding.etUser.hint = "Usuario Admin"
        } else {
            // Estilos para Cliente
            binding.tvTabCliente.setTextColor(android.graphics.Color.WHITE)
            binding.tvTabAdmin.setTextColor(android.graphics.Color.GRAY)
            binding.tvRegistrar.visibility = View.VISIBLE
            binding.etUser.hint = "Correo Electrónico"
        }
    }

    private fun loginFirebase(email: String, pass: String) {
        // Autenticación estándar de Firebase
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                val simpleName = email.substringBefore("@")
                SessionManager.saveSession(this, email, simpleName)
                goToHome()
            } else {
                Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
