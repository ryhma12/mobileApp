package com.example.mobileapp.helpers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserHelper {
    private fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun getCurrentUid(): String {
        return getCurrentUser()?.uid ?: ""
    }

    fun getCurrentUsername(): String {
        return getCurrentUser()?.displayName ?: ""
    }
}