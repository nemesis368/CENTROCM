package com.miraflores.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Referencias a los botones del XML (¡Sin tildes en los IDs!)
        val btnOdontologia = findViewById<Button>(R.id.btnOdontologia)
        val btnPsicologia = findViewById<Button>(R.id.btnPsicologia)
        val btnConsultaGeneral = findViewById<Button>(R.id.btnConsultaGeneral)

        // Listeners
        btnOdontologia.setOnClickListener {
            // Aquí iría el código para ver la lista de Odontología
            Toast.makeText(this, "Gestionar Odontología", Toast.LENGTH_SHORT).show()
        }

        btnPsicologia.setOnClickListener {
            Toast.makeText(this, "Gestionar Psicología", Toast.LENGTH_SHORT).show()
        }

        btnConsultaGeneral.setOnClickListener {
            Toast.makeText(this, "Gestionar Consulta General", Toast.LENGTH_SHORT).show()
        }
    }
}