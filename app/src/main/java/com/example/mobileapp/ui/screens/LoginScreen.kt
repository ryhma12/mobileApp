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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.res.stringResource
import com.example.mobileapp.R
import com.example.mobileapp.model.Account
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(navController: NavController,  onSignInClick: () -> Unit, viewModel: LoginViewModel = viewModel()) {
    var loginChoice by remember { mutableStateOf("login") }
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate("home_route") {
                    popUpTo("login_route") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                val error = (authState as AuthState.Error).message
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(error)
                }
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF476A6F))
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (loginChoice == "login") {
                Login(onLoginChoiceChange = { loginChoice = it }, viewModel = viewModel, onSignInClick)
            } else {
                Register(onLoginChoiceChange = { loginChoice = it }, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun Login(onLoginChoiceChange: (String) -> Unit, viewModel: LoginViewModel, onSignInClick: () -> Unit) {
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
        Button(onClick = { onSignInClick() }) {
            Text("Sign in with google")
        }
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
    var account by remember { mutableStateOf(Account()) }
    var verifyPassword by remember { mutableStateOf("") }
    var tosCheck by remember { mutableStateOf(false) }
    var showTos by remember { mutableStateOf(false) }
    var isCompany by remember { mutableStateOf(false) }

    Column {
        Text("Register", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = account.username,
            onValueChange = { account = account.copy(username = it) },
            label = { Text(
                if(isCompany){
                    "Company name"
                }else{
                    "Username"
                }
            ) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray,
                focusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = account.email,
            onValueChange = { account = account.copy(email = it) },
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
            value = account.password,
            onValueChange = { account = account.copy(password = it) },
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
        Text(
            if(isCompany){
                "I’m registering as a user"
            }else{
                "I’m registering as a company"
            },
            modifier = Modifier
                .clickable {
                    isCompany = !isCompany
                    account = account.copy(type = if (isCompany){"Company"}else{"Influencer"})
                },
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(
                "By registering you agree to the terms of service",
                modifier = Modifier
                    .weight(1f)
                    .clickable { showTos = true }
            )
            Checkbox(tosCheck, onCheckedChange = { tosCheck = it })
        }
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
            Button(
                onClick = {
                    if (tosCheck &&
                        account.username.isNotBlank() &&
                        account.email.isNotBlank() &&
                        account.password.isNotBlank() &&
                        verifyPassword.isNotBlank()
                    ) {
                        viewModel.register(account.email, account.password, verifyPassword, account.username, account.type)
                    }
                },
                enabled = tosCheck
            ) {
                Text("Sign up", color = Color.White)
            }
        }
    }

    if (showTos) {
        TosDialog(onDismiss = { showTos = false })
    }
}


@Composable
fun TosDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Terms of Service")
        },
        text = {
            Column {
                Text(text = stringResource(R.string.tos),modifier = Modifier.verticalScroll(rememberScrollState()))
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        },
        modifier = Modifier.fillMaxWidth(1.0F)
    )
}