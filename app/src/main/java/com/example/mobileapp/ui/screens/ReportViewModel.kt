package com.example.mobileapp.ui.screens

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ReportViewModel(reportedUid: String) : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val reportsRef = firestore.collection("users").document(reportedUid).collection("reports")
    private val updates = mutableMapOf<String, Any>()

    fun updateUserInfo(description: String?) {
        if (!description.isNullOrBlank()) {
            val report = hashMapOf(
                "description" to description,
                "timestamp" to Timestamp.now()
            )

            reportsRef.add(report)
                .addOnSuccessListener {
                    // Optionally show a success message or navigate away
                }
                .addOnFailureListener {
                    // Handle failure (e.g., show error message)
                }
        }
    }
}