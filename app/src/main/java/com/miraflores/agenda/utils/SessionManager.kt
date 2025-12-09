package com.miraflores.agenda.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "AgendaSession"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    private const val KEY_EMAIL = "email"
    private const val KEY_NAME = "name" // <--- Importante

    // Guardar sesión (Nombre y Correo)
    fun saveSession(context: Context, email: String, name: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_NAME, name) // Guardamos el nombre
        editor.apply()
    }

    // Obtener Correo
    fun getEmail(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_EMAIL, "Usuario")
    }

    // Obtener Nombre (NUEVO MÉTODO QUE NECESITAS)
    fun getName(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_NAME, "Usuario Invitado")
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun logout(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
