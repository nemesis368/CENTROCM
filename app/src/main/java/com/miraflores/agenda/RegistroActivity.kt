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

        findViewById<Button>(R.id.btnRegistrar).setOnClickListener {
            val nom = findViewById<EditText>(R.id.etNombreReg).text.toString()
            val tel = findViewById<EditText>(R.id.etTelefonoReg).text.toString()
            val mail = findViewById<EditText>(R.id.etCorreoReg).text.toString()
            val user = findViewById<EditText>(R.id.etUsuarioReg).text.toString()
            val pass = findViewById<EditText>(R.id.etPassReg).text.toString()

            if(nom.isNotEmpty() && user.isNotEmpty() && pass.isNotEmpty()){
                if(dao.registrar(nom, tel, mail, user, pass)){
                    Toast.makeText(this, "Registrado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}