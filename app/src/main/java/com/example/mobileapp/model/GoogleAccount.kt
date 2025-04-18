package com.example.mobileapp.model

data class GoogleAccount (
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?
)