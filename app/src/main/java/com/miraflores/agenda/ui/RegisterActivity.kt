package com.miraflores.agenda.ui

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.miraflores.agenda.databinding.ActivityRegistroBinding
import com.miraflores.agenda.utils.SessionManager

class RegisterActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityRegistroBinding
    // Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()

        setupListeners()
    }

    private fun setupListeners() {
        // Botones de navegación
        binding.btnBack.setOnClickListener { finish() }
        binding.tvIrALogin.setOnClickListener { finish() }

        // Botón de Registrar
        binding.btnRegistrar.setOnClickListener {
            val name = binding.etNombreReg.text.toString().trim()
            val email = binding.etCorreoReg.text.toString().trim()
            val pass = binding.etPassReg.text.toString().trim()

            // 1. Validaciones
            if (name.isEmpty()) {
                binding.etNombreReg.error = "Escribe tu nombre completo"
                binding.etNombreReg.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etCorreoReg.error = "Email inválido"
                binding.etCorreoReg.requestFocus()
                return@setOnClickListener
            }

            if (pass.length < 6) {
                binding.etPassReg.error = "Mínimo 6 caracteres"
                binding.etPassReg.requestFocus()
                return@setOnClickListener
            }

            // 2. Bloquear botón para evitar doble clic
            binding.btnRegistrar.isEnabled = false

            // 3. Llamar a Firebase
            registerUser(email, pass, name)
        }
    }

    private fun registerUser(email: String, pass: String, name: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                // Reactivar botón siempre, sea éxito o error
                binding.btnRegistrar.isEnabled = true

                if (task.isSuccessful) {
                    // --- ÉXITO ---

                    // Guardar sesión local
                    SessionManager.saveSession(this, email, name)

                    // Mensaje de éxito
                    Toast.makeText(this, "¡Usuario creado exitosamente!", Toast.LENGTH_LONG).show()

                    // Limpiar campos visualmente
                    binding.etNombreReg.text?.clear()
                    binding.etCorreoReg.text?.clear()
                    binding.etPassReg.text?.clear()

                    // Poner foco en el primer campo
                    binding.etNombreReg.requestFocus()

                } else {
                    // --- ERROR ---
                    // Obtenemos el mensaje exacto de Firebase (en inglés generalmente)
                    // Ej: "The email address is already in use"
                    val errorMsg = task.exception?.localizedMessage ?: "Error desconocido al registrar"
                    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
                }
            }
    }
}