package com.example.mobileapp.ui.screens.contacts

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

class ContactsViewModel : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()
    private val currentUid = UserHelper().getCurrentUid()

    private companion object {
        const val TAG = "ContactsViewModel"
        const val PATH_CHATS = "chats"
        const val PATH_USER_CHATS = "userChats"
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

    suspend fun getOrCreateChat(selectedUid: String): String {
        val chatId = genChatId(currentUid, selectedUid)
        return if (chatExists(chatId)){
            chatId
        } else {
            createChat(chatId, selectedUid)
        }
    }

    private fun genChatId(uid1: String, uid2: String): String {
        val (minId, maxId) = listOf(uid1, uid2).sorted()
        return "${minId}_${maxId}"
    }

    private suspend fun chatExists(chatId: String): Boolean {
        return try {
            chatsRef.child(chatId).get().await().exists()
        } catch (e: Exception) {
            Log.e(TAG, "chatExists check failed", e)
            false
        }
    }

    private suspend fun createChat(chatId: String, selectedUid: String): String {
        val selectedContact = _contacts.value.firstOrNull { it.uid == selectedUid }
            ?: throw Exception("selectedContact not found")

        val chatData = hashMapOf(
            PATH_CREATED_AT to ServerValue.TIMESTAMP,
            PATH_UPDATED_AT to ServerValue.TIMESTAMP,
            PATH_CHATTERS to mapOf(
                currentUid to ChatterInfo(currentUid, UserHelper().getCurrentUsername()),
                selectedUid to ChatterInfo(selectedUid, selectedContact.name)
            )
        )

        val updates = hashMapOf<String, Any>(
            "$PATH_CHATS/$chatId" to chatData,
            "$PATH_USER_CHATS/$currentUid/$chatId" to true,
            "$PATH_USER_CHATS/$selectedUid/$chatId" to true
        )

        db.reference.updateChildren(updates).await()
        return chatId
    }
}