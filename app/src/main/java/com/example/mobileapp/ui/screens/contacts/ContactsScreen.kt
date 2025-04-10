package com.example.mobileapp.ui.screens.contacts

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobileapp.R
import com.example.mobileapp.model.Contact
import kotlinx.coroutines.launch

@Composable
fun ContactsScreen(navController: NavController) {
    val viewModel = ContactsViewModel()
    val contacts by viewModel.contacts.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(contacts) {
            ContactItem(navController, contact = it, viewModel = viewModel)
        }
    }
}

@Composable
fun ContactItem(
    navController: NavController,
    contact: Contact,
    viewModel: ContactsViewModel,
    modifier: Modifier = Modifier,
){
    val scope = rememberCoroutineScope()
    Card(modifier = modifier) {
        Column (modifier = Modifier){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                ProfilePic(contact.profilePic)
                ContactInfo(contact.name)
                Spacer(Modifier.weight(1f))
                ContactItemButton(
                    onClick = {
                        scope.launch {
                            try {
                                val chatId = viewModel.getChat(contact)
                                navController.navigate("chat_route/${chatId}")
                            } catch (e: Exception) {
                                Log.e("ContactItem", "Failed to open chat",e)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ContactItemButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = ""
        )
    }
}

@Composable
fun ContactInfo(
    name: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = name,
            modifier = Modifier
        )
    }
}

@Composable
fun ProfilePic(
    @DrawableRes profilePic: Int,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(64.dp)
            .padding(dimensionResource(R.dimen.padding_small)),
        painter = painterResource(profilePic),
        contentDescription = null
    )
}