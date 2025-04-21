package com.example.mobileapp.model

data class Account(
    val docId: String = "",
    val uid: String = "",
    val name: String = "",
    val profilePicture: String = "",
    val password: String = "",
    val username: String = "",
    val email: String = "",
    val description: String = "",
    val minPrice: Float = 0f,
    val maxPrice: Float = 0f,
    val type: String = "Influencer",
    val linkedAccounts: List<LinkedAccount> = emptyList(),
    val tags: MutableList<String> = mutableListOf()
)