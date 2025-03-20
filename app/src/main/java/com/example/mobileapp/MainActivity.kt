package com.example.mobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobileapp.ui.theme.MobileAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigation(
                            backgroundColor = Color.White,
                            contentColor = Color.Gray
                        ) {
                            BottomNavigationItem(
                                icon = {
                                    Spacer(modifier = Modifier)
                                },
                                label = { Text("Home") },
                                selected = true,
                                onClick = {}
                            )
                            BottomNavigationItem(
                                icon = {
                                    Spacer(modifier = Modifier)
                                },
                                label = { Text("Notifications") },
                                selected = true,
                                onClick = {}
                            )
                            BottomNavigationItem(
                                icon = {
                                    Spacer(modifier = Modifier)
                                },
                                label = { Text("Profile") },
                                selected = true,
                                onClick = {}
                            )
                            BottomNavigationItem(
                                icon = {
                                    Spacer(modifier = Modifier)
                                },
                                label = { Text("Settings") },
                                selected = true,
                                onClick = {}
                            )
                        }
                    }

                ) { innerPadding ->
                    MobileApp()
                }
            }
        }
    }
}