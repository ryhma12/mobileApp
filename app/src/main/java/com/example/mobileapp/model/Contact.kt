package com.example.mobileapp.model

import androidx.annotation.DrawableRes

data class Contact(
    val id: Int,
    @DrawableRes val profilePic: Int,
    val name: String,
)