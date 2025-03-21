package com.example.mobileapp.model

data class Matches (
    val userIds: List<Int>,
    val matchDate: Long = System.currentTimeMillis(),
    val revokedMatchDate: Long,
    val contractCreationDate: Long
)