package com.example.mobileapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun LoginScreen(navController: NavController) {
    var loginChoice by remember { mutableStateOf("login") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center,

    ) {
        if(loginChoice == "login"){
            Login(onLoginChoiceChange = { loginChoice = it })
        }else{
            Register(onLoginChoiceChange = { loginChoice = it })
        }
    }
}

@Composable
fun Login(onLoginChoiceChange: (String) -> Unit) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Enter username") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray, // Works in Material3
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray, // Works in Material3
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row() {
            Button(onClick = { onLoginChoiceChange("register") }) {
                Text("Register")
            }
            Button(onClick = {}) {
                Text("Login")
            }
        }

    }
}

@Composable
fun Register(onLoginChoiceChange: (String) -> Unit) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("username") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray, // Works in Material3
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("email") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray, // Works in Material3
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray, // Works in Material3
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = verifyPassword,
            onValueChange = { verifyPassword = it },
            label = { Text("Verify password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray, // Works in Material3
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row() {
            Button(onClick = { onLoginChoiceChange("login") }) {
                Text("Login")
            }
            Button(onClick = {}) {
                Text("Sign up")
            }
        }
    }
}
