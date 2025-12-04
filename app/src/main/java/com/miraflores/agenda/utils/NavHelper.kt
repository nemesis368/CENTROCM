package com.miraflores.agenda.utils

import android.app.Activity
import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.miraflores.agenda.R
// Importamos las actividades
import com.miraflores.agenda.ui.MainActivity
import com.miraflores.agenda.ui.AppointmentActivity
import com.miraflores.agenda.ui.SettingsActivity

object NavHelper {
    fun setupNavigation(activity: Activity, bottomNav: BottomNavigationView, selectedId: Int) {

        // Marcar el ítem seleccionado actual para que se pinte de color
        bottomNav.selectedItemId = selectedId

        bottomNav.setOnItemSelectedListener { item ->
            // Si tocamos el botón de la pantalla en la que ya estamos, no hacemos nada
            if (item.itemId == selectedId) return@setOnItemSelectedListener true

            val intent = when (item.itemId) {
                R.id.nav_inicio -> Intent(activity, MainActivity::class.java)
                R.id.nav_citas -> Intent(activity, AppointmentActivity::class.java)
                R.id.nav_clientes -> Intent(activity, AppointmentActivity::class.java)
                R.id.nav_ajustes -> Intent(activity, SettingsActivity::class.java)
                else -> null
            }

            intent?.let {
                // FLAGS para que la transición sea suave y no acumule pantallas en memoria
                it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(it)
                activity.overridePendingTransition(0, 0) // Elimina animación de parpadeo
            }
            true
        }
    }
}