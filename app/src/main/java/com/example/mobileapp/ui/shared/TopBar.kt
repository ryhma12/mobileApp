package com.example.mobileapp.ui.shared

import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController){
    var menuExpanded: Boolean by remember { mutableStateOf(false) }
    TopAppBar(
        title = {/*TODO*/},
        actions = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, "Menu")
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
                            restoreState = true
                        }
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = {
                        /*TODO*/
                        menuExpanded = false
                    }
                )
            }
        }
    )
}