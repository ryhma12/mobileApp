package com.example.mobileapp.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.model.Chat
import com.example.mobileapp.model.ChatterInfo
import com.example.mobileapp.model.Message
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatViewModel(chatId: String) : ViewModel() {

    private val _chat = MutableStateFlow(Chat(chatId = chatId))
    val chat: StateFlow<Chat> = _chat.asStateFlow()

    private companion object {
        const val TAG = "ChatViewModel"
        const val PATH_CHATS = "chats"
        const val PATH_CHATTERS = "chatters"
        const val PATH_MESSAGES = "messages"
        const val PATH_CREATED_AT = "createdAt"
        const val PATH_UPDATED_AT = "updatedAt"
    }

    private val db = FirebaseDatabase.getInstance()
    private val chatRef = db.getReference("$PATH_CHATS/$chatId")
    private val messagesRef = chatRef.child(PATH_MESSAGES)

    init {
        setupFirebaseListeners()
    }

    private fun setupFirebaseListeners() {
        //setupChatMetadataListener()
        setupMessagesListener()
    }

    /*private fun setupChatMetadataListener() {
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatters = snapshot.child(PATH_CHATTERS).children.mapNotNull { child ->
                    child.key?.let { key ->
                        child.getValue<ChatterInfo>()?.let { value ->
                            key to value
                        }
                    }
                }.toMap()

                val createdAt = snapshot.child(PATH_CREATED_AT).getValue<Long>() ?: 0L
                val updatedAt = snapshot.child(PATH_UPDATED_AT).getValue<Long>() ?: 0L
                _chat.update { current ->
                    current.copy(
                        chatters = chatters,
                        createdAt = createdAt,
                        updatedAt = updatedAt
                    )
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Chat metadata listener cancelled: ${error.message}")
            }
        })
    }*/

    private fun setupMessagesListener() {
        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, prevKey: String?) {
                handleMessageUpdate(snapshot)
            }
            override fun onChildChanged(snapshot: DataSnapshot, prevKey: String?) {
                handleMessageUpdate(snapshot)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val messageId = snapshot.key
                if (messageId != null) {
                    removeMessageFromChat(messageId)
                }
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Messages listener cancelled: ${error.message}")
            }
            private fun handleMessageUpdate(snapshot: DataSnapshot) {
                val message = snapshot.getValue<Message>()
                message?.let{ updateMessageInChat(it) }
            }
        })
    }
    private fun updateMessageInChat(message: Message) {
        _chat.update { current ->
            val messages = current.messages.toMutableList().apply {
                removeAll { it.messageId == message.messageId }
                add(message)
                sortBy { it.timestamp }
            }
            current.copy(messages = messages)
        }
    }

    private fun removeMessageFromChat(messageId: String) {
        _chat.update { current ->
            val messages = current.messages.toMutableList().apply {
                removeAll { it.messageId == messageId }
            }
            current.copy(messages = messages)
        }
    }

    fun sendMessage(text: String, senderId: String) {
        viewModelScope.launch {
            // Generates id for message
            val messageId = messagesRef.push().key
            if (messageId == null) {
                Log.e(TAG, "Cant get push key")
                return@launch
            }
            val newMessage = Message(
                messageId = messageId,
                content = text,
                senderId = senderId,
            )

            val updates = hashMapOf(
                "$PATH_MESSAGES/$messageId" to newMessage,
                PATH_UPDATED_AT to ServerValue.TIMESTAMP
            )
            try {
                chatRef.updateChildren(updates).await()
                Log.d(TAG, "Message sent! id: ${newMessage.messageId}")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send message: ${e.message}", e)
            }
        }
    }
}