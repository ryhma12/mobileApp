package com.example.mobileapp

data class Account(
    val name: String,
    val profilePicture: String,
    val password: String,
    val userName: String,
    val email: String,
    val description: String,
    val minPrice: Float,
    val maxPrice: Float,
    val linkedAccounts: List<LinkedAccount> = emptyList(),
    val tags: MutableList<String> = mutableListOf()
)