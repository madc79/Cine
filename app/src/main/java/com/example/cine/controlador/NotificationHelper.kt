package com.example.cine.controlador

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "example_channel"
        const val NOTIFICATION_ID = 1
        const val sharedPrefFile = "com.example.cine.preferences"
    }

    // Crear el canal de notificación (necesario para Android 8.0+)
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Example Channel"
            val descriptionText = "This is an example notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Construir y mostrar la notificación
    fun showNotification(title: String, message: String) {
        // Leer el valor de las preferencias (si las notificaciones están habilitadas o no)
        val sharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val isNotificationsEnabled = sharedPreferences.getBoolean("notificacion", false)

        // Solo mostramos la notificación si está habilitada en las preferencias
        if (isNotificationsEnabled) {


            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Ícono de la notificación
                .setContentTitle(title) // Título de la notificación
                .setContentText(message) // Texto del mensaje
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Prioridad de la notificación
                .setAutoCancel(true) // La notificación desaparece al tocarla

            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }
}
