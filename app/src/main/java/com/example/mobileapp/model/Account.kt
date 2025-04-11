package com.example.mobileapp.model

import androidx.annotation.DrawableRes
import com.example.mobileapp.R

data class Account(
    val name: String,
    @DrawableRes val profilePic: Int = R.drawable.account,
    val password: String,
    val userName: String,
    val email: String,
    val description: String,
    val minPrice: Float,
    val maxPrice: Float,
    val linkedAccounts: List<LinkedAccount> = emptyList(),
    val tags: MutableList<String> = mutableListOf()
)