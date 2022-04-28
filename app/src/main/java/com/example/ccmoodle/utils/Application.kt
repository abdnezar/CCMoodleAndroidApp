package com.example.ccmoodle.utils

import android.app.Application
import android.content.Context
import com.example.ccmoodle.utils.Helper.Companion.updateUserToken
import com.example.ccmoodle.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Application : Application() {
    private var auth : FirebaseAuth? = null
    private var user : FirebaseUser? = null

    override fun onCreate() {
        super.onCreate()

        auth = Firebase.auth
        user = auth?.currentUser

        checkUserToken()
    }

    private fun checkUserToken() {
        try {
            if (user?.uid != null) {
                val sharedPref = getSharedPreferences(User.SHARED_PREF_NAME, Context.MODE_PRIVATE)
                val token = sharedPref.getString("token", null)
                if (token == null) {
                    updateUserToken(applicationContext, user!!.uid)
                }
            }
        } catch (e: Exception) {
            return
        }
    }
}