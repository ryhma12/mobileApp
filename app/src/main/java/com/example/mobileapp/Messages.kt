package com.example.mobileapp

data class Messages (
    val messageContent: String,
    val timestamp: Long = System.currentTimeMillis(),
    val senderId: Int,
    val receiverId: Int
)