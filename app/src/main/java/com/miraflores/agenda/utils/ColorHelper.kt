package com.miraflores.agenda.utils

import android.content.Context
import android.graphics.Color
import android.view.View

object ColorHelper {
    private const val PREF_NAME = "AppConfig"
    private const val KEY_BG_COLOR = "BG_COLOR_HEX"
    private const val DEFAULT_COLOR = "#3E0018" // Color Vino por defecto

    fun applyBackgroundColor(context: Context, view: View?) {
        if (view == null) return

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val hexColor = prefs.getString(KEY_BG_COLOR, DEFAULT_COLOR) ?: DEFAULT_COLOR

        try {
            view.setBackgroundColor(Color.parseColor(hexColor))
        } catch (e: Exception) {
            view.setBackgroundColor(Color.parseColor(DEFAULT_COLOR))
        }
    }
}