package com.example.mobileapp.ui.screens.contract

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobileapp.model.Contact
import com.example.mobileapp.ui.screens.contacts.ContactsViewModel

@Composable
fun CreateContractScreen(
    viewModel: CreateContractViewModel
) {
    ContractForm(viewModel)
}

@Composable
fun ContractForm(viewModel: CreateContractViewModel) {
    var price by remember { mutableStateOf("") }
    val recipient =  viewModel.user?.username
    val company = "Your Company"


    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF476A6F))
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text("Contract", fontSize = 28.sp)
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    value = company,
                    onValueChange = {},
                    label = { Text("Company") },
                    singleLine = true,
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.LightGray,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    value = recipient ?: "something went wrong retrieving recipient",
                    onValueChange = {},
                    label = { Text("Contract Recipient") },
                    singleLine = true,
                    enabled = false,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.LightGray,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    value = price,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            price = newValue
                        }
                    },
                    label = { Text("Price") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.LightGray,
                        focusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = {
                        //viewModel.createContract(price)
                    }) {
                        Text("Create Contract", color = Color.White)
                    }
                }
            }
        }
    }
}
