package com.example.mobileapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ContactsScreen(navController: NavController) {
    Box(modifier = Modifier.background(color = Color.Cyan).fillMaxSize()) {
        Text("Contacts screen", modifier = Modifier.align(Alignment.Center), fontSize = 32.sp)
    }
}