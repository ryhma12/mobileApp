package com.example.mobileapp.model

import androidx.annotation.DrawableRes
import com.example.mobileapp.R

data class Contact(
    val uid: String,
    @DrawableRes val profilePic: Int = R.drawable.bioplaceholder,
    val name: String,
)