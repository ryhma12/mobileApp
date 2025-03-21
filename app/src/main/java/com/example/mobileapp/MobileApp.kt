package com.example.mobileapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mobileapp.ui.screens.HomeScreen

@Composable
fun MobileApp () {
    Column(modifier = Modifier.fillMaxSize()) {
        HomeScreen()
    }
}