package com.example.mobileapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import android.util.Log


@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    var loginChoice by remember { mutableStateOf("login") }
    val authState by viewModel.authState.collectAsState()

    Log.i("auth","authstate: $authState")

    LaunchedEffect(authState) {
        Log.d("auth","authstate: $authState")
        if(authState is AuthState.Success) {
            navController.navigate("home_route") {
                popUpTo("login_route") { inclusive = true }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF476A6F)),
        contentAlignment = Alignment.Center,

    ) {
        if(loginChoice == "login"){
            Login(onLoginChoiceChange = { loginChoice = it }, viewModel = viewModel)
        }else{
            Register(onLoginChoiceChange = { loginChoice = it }, viewModel = viewModel)
        }
    }
}

@Composable
fun Login(onLoginChoiceChange: (String) -> Unit, viewModel: LoginViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(

    ) {
        Text("Login", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter email") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray,
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
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
            Text(
                "Don't have an account? Register.",
                modifier = Modifier.clickable { onLoginChoiceChange("register") },
                color = Color.Blue
            )
            Button(onClick = { viewModel.login(email, password) }) {
                Text("Login", color = Color.White)
            }
        }

    }
}

@Composable
fun Register(onLoginChoiceChange: (String) -> Unit, viewModel: LoginViewModel) {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verifyPassword by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
    ) {
        Text("Register", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = userName,
            onValueChange = { userName = it },
            label = { Text("username") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray,
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = email,
            onValueChange = { email = it },
            label = { Text("email") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray,
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = password,
            onValueChange = { password = it },
            label = { Text("password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray,
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = verifyPassword,
            onValueChange = { verifyPassword = it },
            label = { Text("Verify password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
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
            Text(
                "Already have an account? Login.",
                modifier = Modifier.clickable { onLoginChoiceChange("login") },
                color = Color.Blue
                )
            Button(onClick = { viewModel.register(email, password, userName) }) {
                Text("Sign up" , color = Color.White)
            }
        }
    }
}
