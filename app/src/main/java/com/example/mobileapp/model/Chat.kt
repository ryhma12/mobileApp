package com.example.mobileapp.model

import java.io.Serializable
import java.util.UUID

data class Chat(
    val chatId: String = UUID.randomUUID().toString(),
    val chatters: Map<String, ChatterInfo> = emptyMap(),
    val messages: Map<String, Message> = emptyMap(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Serializable

data class ChatterInfo(
    val userId: String = "",
    val username: String = "",
    val joinedAt: Long = System.currentTimeMillis()
)
