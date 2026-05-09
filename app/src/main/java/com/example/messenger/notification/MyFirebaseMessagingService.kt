package com.example.messenger.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.messenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: "New message"
        val body = message.notification?.body ?: ""
        println("FCM LOG: ПРИЛЕТЕЛ ПУШ! Заголовок: $title, Текст: $body")
        showNotification(title, body)
    }
    override fun onNewToken(token: String){
        super.onNewToken(token)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if(uid != null) {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .update("fcmToken", token)
        }
    }
    private fun showNotification(title: String, message: String) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "chat_message"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Messages",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()
        manager.notify(Random.nextInt(), notification)
    }
}