package com.example.mobileapp.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.helpers.UserHelper
import com.example.mobileapp.model.Account
import com.example.mobileapp.model.ChatterInfo
import com.example.mobileapp.model.Contact
import com.example.mobileapp.model.Match
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MatchViewModel : ViewModel() {
    private val _matches = MutableStateFlow<List<Account>>(emptyList())
    val matches: StateFlow<List<Account>> = _matches.asStateFlow()
    private val currentUid = UserHelper().getCurrentUid()

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    init {
        loadMatches()
    }
    // Doesnt have matching yet
    private fun loadMatches() {
        viewModelScope.launch {
            val snapshot = firestore.collection("matches")
                .whereArrayContains("users", currentUid)
                .get()
                .await()

            _matches.value = snapshot.documents.mapNotNull { doc ->
                val usersAndI = doc.get("users") as List<*>
                val users = usersAndI.firstOrNull { it != currentUid } as String
                users.let {
                    val userSnapshot = usersCollection.document(it).get().await()
                    userSnapshot.toObject<Account>()
                }
            }
        }
    }
}