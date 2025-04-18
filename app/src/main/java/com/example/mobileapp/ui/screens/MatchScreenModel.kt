package com.example.mobileapp.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.helpers.UserHelper
import com.example.mobileapp.model.Account
import com.example.mobileapp.model.ChatterInfo
import com.example.mobileapp.model.Contact
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MatchViewModel : ViewModel() {
    private val _contacts = MutableStateFlow<List<Account>>(emptyList())
    val contacts: StateFlow<List<Account>> = _contacts.asStateFlow()
    private val currentUid = UserHelper().getCurrentUid()

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    init {
        loadContacts()
    }
    // Doesnt have matching yet
    private fun loadContacts() {
        viewModelScope.launch {
                val snapshot = usersCollection.get().await()
                _contacts.value = snapshot.documents.mapNotNull { document ->
                    // Filters everyone other than yourself atm
                    val userId = document.id.takeIf { it != currentUid }
                    userId?.let {
                        Account(
                            uid = it,
                            name = document.getString("username") ?: "Unknown"
                        )
                    }
                }
        }
    }
}