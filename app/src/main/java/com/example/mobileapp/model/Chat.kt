package com.example.mobileapp.model

data class Chat(
    val chatId: String,
    val chatters: Map<String, ChatterInfo> = emptyMap(), // uid info
    val messages: List<Message> = emptyList(),
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)

data class ChatterInfo(
    val userId: String = "",
    val username: String = ""
)