package com.sector.cityguide.main

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth: FirebaseAuth

class MyApplication(private val context: Context): Application() {

    fun initFirebase() {
        FirebaseApp.initializeApp(context)
        auth = FirebaseAuth.getInstance()
    }

    fun isLogged() = auth.currentUser
    fun getUid() = auth.uid!!
}