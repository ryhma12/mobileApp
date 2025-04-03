package com.example.mobileapp.model

import java.util.UUID

data class Message(
    val messageId: String = UUID.randomUUID().toString(),
    val content: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
