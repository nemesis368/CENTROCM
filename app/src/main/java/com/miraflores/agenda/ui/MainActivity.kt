package com.miraflores.agenda.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.miraflores.agenda.R
import com.miraflores.agenda.databinding.ActivityMainBinding
import com.miraflores.agenda.utils.ColorHelper
import com.miraflores.agenda.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Color de Fondo
        ColorHelper.applyBackgroundColor(this, binding.mainContentLayout)

        setupUI()
        setupDashboardListeners()
        setupDrawer()
    }

    override fun onResume() {
        super.onResume()
        ColorHelper.applyBackgroundColor(this, binding.mainContentLayout)
    }

    private fun setupUI() {
        Glide.with(this).asGif().load(R.drawable.dos).into(binding.ivUserIcon)
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEEE, d MMM", Locale("es", "ES"))
        binding.tvDate.text = dateFormat.format(currentDate).uppercase()
        val userName = SessionManager.getName(this)
        binding.tvUserName.text = userName

        val headerView = binding.navView.getHeaderView(0)
        val navName = headerView.findViewById<TextView>(R.id.nav_header_name)
        val navEmail = headerView.findViewById<TextView>(R.id.nav_header_email)
        navName?.text = userName
        navEmail?.text = SessionManager.getEmail(this)
    }

    private fun setupDrawer() {
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_inicio -> Toast.makeText(this, "Inicio", Toast.LENGTH_SHORT).show()
                R.id.nav_citas -> startActivity(Intent(this, AppointmentActivity::class.java))
                // Clientes eliminado del menú lateral también si así lo deseas
                R.id.nav_ajustes -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.nav_logout -> mostrarDialogoCerrarSesion()
                R.id.nav_info -> Toast.makeText(this, "Información de Sede", Toast.LENGTH_SHORT).show()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setupDashboardListeners() {
        // 1. Agendar -> Abre formulario (AppointmentActivity)
        binding.btnAgenda.setOnClickListener {
            startActivity(Intent(this, AppointmentActivity::class.java))
        }

        // 2. Ver Citas -> Abre HISTORIAL (AppointmentsListActivity) <-- ¡AQUÍ ESTABA EL ERROR!
        binding.btnVerCitas.setOnClickListener {
            startActivity(Intent(this, AppointmentsListActivity::class.java))
        }

        // 3. Mapa -> Abre Mapa (MapsActivity)
        binding.btnMapa.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun mostrarDialogoCerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro que deseas salir?")
            .setPositiveButton("Sí") { _, _ ->
                SessionManager.logout(this)
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}