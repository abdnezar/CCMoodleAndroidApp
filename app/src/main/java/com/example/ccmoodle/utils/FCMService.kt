package com.example.ccmoodle.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        Firebase.firestore.collection(User.USERS_COLLECTION).document(Firebase.auth.currentUser!!.uid).set(mapOf("token" to token))
        Helper.subscribeToTopic("all")
        Log.e("fcm", "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        NotificationManager.sendNotification(applicationContext,
            message.notification?.title ?: "New Notification",
            message.notification?.body)
    }
}