package com.example.mobileapp.ui.screens

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UpdateUserInfoViewModel() : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val user = firebaseAuth.currentUser
    private val firestore = FirebaseFirestore.getInstance()
    private val userRef = user?.let {
        firestore.collection("users").document(it.uid)
    }
    private val updates = mutableMapOf<String, Any>()

    fun updateUserInfo(newUsername: String?, newEmail: String?, description: String?, minPrice: Float?, maxPrice: Float?) {
        updates.clear()

        newUsername?.let { updates["username"] = it }
        newEmail?.let { updates["email"] = it }
        minPrice?.let { updates["minPrice"] = it }
        maxPrice?.let { updates["maxPrice"] = it }
        //description?.let { updates["description"] = it }

        if(!description.isNullOrBlank()){
            val mUser = FirebaseAuth.getInstance().currentUser
            mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result.token
                    // Send token to your backend via HTTPS
                    // ...
                } else {
                    // Handle error -> task.getException();
                }
            }
        }

        if(updates.isNotEmpty()){
            userRef?.update(updates)
                ?.addOnSuccessListener {
                    //update ui
                }
                ?.addOnFailureListener{
                    //update ui
                }
        }
    }
}