package com.example.mobileapp.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mobileapp.NavItem
import com.example.mobileapp.ui.screens.LoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currentRoute: String?,
    navController: NavController,
    navItems: List<NavItem>,
    loginViewModel: LoginViewModel
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val bottomBarRoutes = navItems.filter { it.showInBottomBar }.map { it.route }
    val showBackButton = currentRoute !in bottomBarRoutes
    val coroutineScope = rememberCoroutineScope()
    val showCreateContractButton = currentRoute == "chat_route/{chatId}"


    TopAppBar(
        title = { "" },
        navigationIcon = {
            if (showBackButton) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    if (showCreateContractButton) {
                        Button (onClick = {
                            navController.navigate("create_contract_route")
                        }) {
                            Text("Create Contract")
                        }
                    }
                }
            }
        },
        actions = {
            if (currentRoute in bottomBarRoutes) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu"
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Settings") },
                        onClick = {
                            navController.navigate("settings_route") {
                                launchSingleTop = true
                            }
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Logout") },
                        onClick = {
                            coroutineScope.launch {
                                loginViewModel.signOut()
                                navController.navigate("login_route") {
                                    popUpTo(0)
                                }
                            }
                            menuExpanded = false
                        }
                    )
                }
            }
        }
    )
}
