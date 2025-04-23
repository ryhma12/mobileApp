package com.example.mobileapp.ui.screens

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobileapp.R
import com.example.mobileapp.model.Account
import kotlinx.coroutines.launch

@Composable
fun MatchScreen(navController: NavController, viewModel: MatchViewModel) {

  //  Text("Matches", modifier = Modifier.align(Alignment.Center), fontSize = 32.sp)
    val matches by viewModel.matches.collectAsState()
    if(matches.isNotEmpty()) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(matches) {
                MatchItem(navController, match = it, viewModel = viewModel)
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
    }
}

@Composable
fun MatchItem(
    navController: NavController,
    match: Account,
    viewModel: MatchViewModel,
    modifier: Modifier = Modifier,
){
    val scope = rememberCoroutineScope()
    Card(modifier = Modifier.fillMaxWidth().height(70.dp).padding(horizontal = dimensionResource(R.dimen.padding_small))) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .size(width = 300.dp, height = 70.dp)
                .clickable {
                    val uid= match.uid
                    navController.navigate("bio_route/${uid}") //passing the uid to the bioscreen, so that we can get the proper info from the right user
                }
        ) {
            ProfilePic(R.drawable.bioplaceholder)
            MatchInfo(match.name)
            Spacer(Modifier.weight(1f))
            MatchItemButton(
                onClick = {
                    scope.launch {
                        try {
                            viewModel.acceptMatch(match.uid)
                        } catch (e: Exception) {
                            Log.e("MatchItem","Failed to accept match", e)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun MatchItemButton(
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
fun MatchInfo(
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