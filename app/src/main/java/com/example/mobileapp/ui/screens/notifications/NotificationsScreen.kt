package com.example.mobileapp.ui.screens.notifications

import NotificationsViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@Composable
fun NotificationsScreen(viewModel: NotificationsViewModel = viewModel()) {
    var searchInput by remember { mutableStateOf("") }
    val contracts by viewModel.contracts.collectAsState()

    val tabs = listOf("Pending", "Ongoing", "Archive")
    var selectedTab by remember { mutableStateOf("Pending") }
    val filteredContracts = contracts.filter { it.status.equals(selectedTab, ignoreCase = true) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.background(color = Color(0xFF476A6F)).fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                tabs.forEach { tab ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (tab == selectedTab) Color(0xFF6A8EAE) else Color(0xFFB0C4DE))
                            .fillMaxHeight()
                            .clickable { selectedTab = tab },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab,
                            color = if (tab == selectedTab) Color.White else Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredContracts) { contract ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = contract.company, modifier = Modifier.clickable {
                                coroutineScope.launch {
                                    viewModel.generatePDF(contract.price, contract.recipient?:"", contract.company?:"", context)
                                }
                            })

                            when (contract.status.lowercase()) {
                                "pending" -> {
                                    Row {
                                        IconButton(onClick = {
                                            viewModel.declineContract(contract.contractId)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Decline"
                                            )
                                        }
                                        IconButton(onClick = {
                                            viewModel.acceptContract(contract.contractId)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Accept"
                                            )
                                        }
                                    }
                                }

                                "ongoing" -> {
                                    IconButton(onClick = {
                                        viewModel.closeContract(contract.contractId)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "close contract"
                                        )
                                    }
                                }
                            }
                        }

                    }

                }
            }
        }
    }