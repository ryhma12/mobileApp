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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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

    var minPriceInput by remember { mutableStateOf("") }
    var maxPriceInput by remember { mutableStateOf("") }

    Column {
        Text("Update your price range")

        TextField(
            value = minPriceInput,
            onValueChange = { minPriceInput = it },
            placeholder = { Text("min price") },
            label = { Text("Min Price") }
        )

        TextField(
            value = maxPriceInput,
            onValueChange = { maxPriceInput = it },
            placeholder = { Text("max price") },
            label = { Text("Max Price") }
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("description") },
            label = { Text("Description") }
        )

        Row {
            Button(onClick = {
                val minPrice = minPriceInput.toFloatOrNull()
                val maxPrice = maxPriceInput.toFloatOrNull()

                if (minPrice != null && maxPrice != null) {
                    viewModel.updateUserInfo(
                        null,
                        null,
                        description,
                        minPrice,
                        maxPrice
                    )
                } else {
                    // Show error, toast, or validation message
                }
            }) {
                Text("Done")
            }
        }
    }
}