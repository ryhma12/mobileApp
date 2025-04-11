package com.example.mobileapp.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.helpers.UserHelper
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

class MatchScreenModel : ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()
    private val currentUid = UserHelper().getCurrentUid()

    private companion object {
        const val TAG = "ContactsViewModel"
        const val PATH_CHATS = "chats"
        const val PATH_CHATTERS = "chatters"
        const val PATH_CREATED_AT = "createdAt"
        const val PATH_UPDATED_AT = "updatedAt"
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    private val db = FirebaseDatabase.getInstance()
    private val chatsRef = db.getReference(PATH_CHATS)

    init {
        loadContacts()
    }

    // Doesnt have matching yet
    private fun loadContacts() {
        viewModelScope.launch {
            try {
                val snapshot = usersCollection.get().await()
                _contacts.value = snapshot.documents.mapNotNull { document ->
                    // Filters everyone other than yourself atm
                    val userId = document.id.takeIf { it != currentUid }
                    userId?.let {
                        Contact(
                            uid = it,
                            name = document.getString("username") ?: "Unknown"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load contacts", e)
            }
        }
    }
}