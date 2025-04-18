package com.example.mobileapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ReportUserScreen(navController: NavController, viewModel: UpdateUserInfoViewModel = viewModel()) {
    var description by remember { mutableStateOf("") }

    Column(
    ) {
        Column(
        ) {
            Text("reason for reporting: ")
            Text(text = "select reason from below, or give the reason yourself: ")
        }
        Button(onClick = {
            description = "Inappropriate/unprofessional messaging"
        }, modifier = Modifier.padding(top = 10.dp)) { Text("Inappropriate/unprofessional messaging") }
        Button(onClick = {
            description = "fake or deceiving profile"
        }, modifier = Modifier.padding(top = 10.dp)) { Text("fake or deceiving profile") }
        Button(onClick = {
            description = "User scammed us / did not meet the requirements of the contract"
        }, modifier = Modifier.padding(top = 10.dp)) { Text("User scammed us / did not meet the requirements of the contract") }
        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("reason for reporting") },
            maxLines = 120,
            minLines = 8,
            modifier = Modifier.padding(top = 10.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row() {
            Button(onClick = { viewModel.updateUserInfo(null, null, description) }) {
                Text("report")
            }
        }
    }
}
