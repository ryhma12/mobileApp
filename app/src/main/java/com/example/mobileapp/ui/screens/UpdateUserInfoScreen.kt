package com.example.mobileapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun UpdateUserInfoScreen(navController: NavController, viewModel: UpdateUserInfoViewModel = viewModel()) {
    var description by remember { mutableStateOf("") }

    Column(
    ) {
        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("description") },
            maxLines = 20,
            minLines = 8,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row() {
            Button(onClick = { viewModel.updateUserInfo(null, null, description) }) {
                Text("Done")
            }
        }
    }
}