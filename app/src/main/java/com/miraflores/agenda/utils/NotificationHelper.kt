package com.miraflores.agenda.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.miraflores.agenda.R

object NotificationHelper {

    private const val CHANNEL_ID = "agenda_channel_high" // Cambié el ID para forzar actualización
    private const val CHANNEL_NAME = "Avisos Importantes"

    fun showNotification(context: Context, title: String, message: String) {
        // 1. Crear el canal antes de notificar
        createNotificationChannel(context)

        // 2. Verificar Permisos (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return // Si no hay permiso, no explota, pero no notifica
            }
        }

        // 3. Construir la notificación
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_warning) // Icono del sistema visible
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message)) // Para textos largos
            .setPriority(NotificationCompat.PRIORITY_HIGH) // ALTA PRIORIDAD
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Sonido y Vibración
            .setAutoCancel(true)

        // 4. Lanzar
        try {
            // Usamos un ID único basado en el tiempo para que no se reemplacen
            val uniqueId = System.currentTimeMillis().toInt()
            NotificationManagerCompat.from(context).notify(uniqueId, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH // IMPORTANTE: HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notificaciones de confirmación de citas"
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}