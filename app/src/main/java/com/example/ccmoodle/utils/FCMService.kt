package com.example.ccmoodle.utils

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import org.json.JSONObject

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        Firebase.firestore.collection(User.USERS_COLLECTION).document(Firebase.auth.currentUser!!.uid).set(mapOf("token" to token))
        Helper.subscribeToTopic("all")
        Log.e("fcm", "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        NotificationManager.sendNotification(applicationContext, message.data["title"] ?: "New Notification", message.data["body"])
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        fun sendRemoteNotification(title: String, body: String, token: String? = null, topic: String? = null) {
            val url = "https://fcm.googleapis.com/fcm/send"
            val at =
                object : AsyncTask<Void, Void, String>() {
                    override fun doInBackground(vararg params: Void?): String {
                        val JSON = MediaType.parse("application/json; charset=utf-8");
                        val client = OkHttpClient()
                        val json = JSONObject()
                        val jsonData = JSONObject()
                        try {
                            jsonData.put("body", body)
                            jsonData.put("title", title)
                            json.put("data", jsonData)
                            if (topic != null) {
                                json.put("to", "/topics/$topic")
                            } else {
                                json.put("to", token)
                            }

                            val body = RequestBody.create(JSON, json.toString())
                            val request = Request.Builder()
                                .header("Authorization", "key=AAAA67LZW5A:APA91bErvBqint1Eq6saNLdhQraZ6_wvhCgTnlRA3kYHxKkj-AJNlsWufHe0Re8N09KhQh3XpbE7NN0Z0Cw_hVMdSwNN5Yop8awI27HuvqlY3a037FAooASBgirfpD9t2cslFXsJH3S2")
                                .header("Content-Type", "application/json")
                                .url(url)
                                .post(body)
                                .build()
                            val response = client.newCall(request).execute()
                            val finalResponse: String = response.body().string()
                            Log.e("fcm", "sendRemoteNotification: $finalResponse")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        return "Void"
                    }
                }.execute()
        }
    }
}