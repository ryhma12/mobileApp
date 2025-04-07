package com.example.mobileapp.model

data class Message(
    val messageId: String = "",
    val content: String = "",
    val senderId: String = "",
    val timestamp: Long = 0L
)
