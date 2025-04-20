package com.example.mobileapp.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mobileapp.R
import androidx.navigation.NavController
import com.example.mobileapp.model.Account
import com.example.mobileapp.ui.screens.contacts.ContactInfo
import com.example.mobileapp.ui.screens.contacts.ContactItem
import com.example.mobileapp.ui.screens.contacts.ProfilePic

@Composable
fun MatchScreen(navController: NavController) {
    Box(modifier = Modifier.background(color = Color.Cyan).fillMaxSize()) {
      //  Text("Matches", modifier = Modifier.align(Alignment.Center), fontSize = 32.sp)
        val viewModel = MatchViewModel()
        val contacts by viewModel.contacts.collectAsState()
        LazyColumn(Modifier.align(Alignment.Center),verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(contacts) {
                MatchItem(navController, contact = it, viewModel = viewModel)
            }
        }

    }
}
@Composable
fun MatchItem(
    navController: NavController,
    contact: Account,
    viewModel: MatchViewModel,
    modifier: Modifier = Modifier,
){
 //   val scope = rememberCoroutineScope()
    Box() {
        Card(modifier = Modifier.size(width = 350.dp, height = 70.dp)) {
            Column(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_small))
                        .size(width = 300.dp, height = 70.dp)
                ) {
                    ProfilePic(R.drawable.bioplaceholder)
                    ContactInfo(contact.name)
                    Spacer(Modifier.weight(1f))
                    ContactItemButton(
                        onClick = {
                            val uid= contact.uid //getting the current uid of the user
                                    navController.navigate("bio_route/${uid}") //passing the uid to the bioscreen, so that we can get the proper info from the right user
                        }
                    )
                }

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