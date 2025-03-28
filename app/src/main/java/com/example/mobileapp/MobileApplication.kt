package com.example.mobileapp

import android.app.Application
import com.google.firebase.FirebaseApp

class MobileApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}