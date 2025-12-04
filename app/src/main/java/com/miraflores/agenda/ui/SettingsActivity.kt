package com.miraflores.agenda.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.miraflores.agenda.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    // Colores disponibles
    private val colorWine = "#3E0018"
    private val colorGreen = "#002814"
    private val colorBlue = "#00183E"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("AppConfig", Context.MODE_PRIVATE)

        loadState()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        // CAMBIO DE COLOR
        binding.rgBackgroundColor.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                com.miraflores.agenda.R.id.rbColorWine -> saveColor(colorWine, "WINE")
                com.miraflores.agenda.R.id.rbColorGreen -> saveColor(colorGreen, "GREEN")
                com.miraflores.agenda.R.id.rbColorBlue -> saveColor(colorBlue, "BLUE")
            }
        }

        // CAMBIO DE TEMA
        binding.rgTheme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                com.miraflores.agenda.R.id.rbLight -> updateTheme(AppCompatDelegate.MODE_NIGHT_NO, "LIGHT")
                com.miraflores.agenda.R.id.rbDark -> updateTheme(AppCompatDelegate.MODE_NIGHT_YES, "DARK")
                com.miraflores.agenda.R.id.rbSystem -> updateTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, "SYSTEM")
            }
        }
    }

    private fun saveColor(hex: String, name: String) {
        // Aplica el color inmediatamente para ver el cambio
        try {
            binding.rootLayout.setBackgroundColor(Color.parseColor(hex))
        } catch (e: Exception) {}

        // Guarda en memoria
        sharedPreferences.edit()
            .putString("BG_COLOR_HEX", hex)
            .putString("BG_COLOR_NAME", name)
            .apply()
    }

    private fun updateTheme(mode: Int, name: String) {
        AppCompatDelegate.setDefaultNightMode(mode)
        sharedPreferences.edit().putString("THEME_MODE", name).apply()
    }

    private fun loadState() {
        // Cargar Color
        val colorName = sharedPreferences.getString("BG_COLOR_NAME", "WINE")
        val hex = sharedPreferences.getString("BG_COLOR_HEX", colorWine)

        when(colorName) {
            "WINE" -> binding.rbColorWine.isChecked = true
            "GREEN" -> binding.rbColorGreen.isChecked = true
            "BLUE" -> binding.rbColorBlue.isChecked = true
        }

        try {
            binding.rootLayout.setBackgroundColor(Color.parseColor(hex))
        } catch (e: Exception) {}

        // Cargar Tema
        val theme = sharedPreferences.getString("THEME_MODE", "SYSTEM")
        when(theme) {
            "LIGHT" -> binding.rbLight.isChecked = true
            "DARK" -> binding.rbDark.isChecked = true
            "SYSTEM" -> binding.rbSystem.isChecked = true
        }
    }
}