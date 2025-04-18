package com.example.mobileapp.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.mobileapp.MainActivity
import com.example.mobileapp.R
import com.example.mobileapp.model.Account
import com.example.mobileapp.model.Contact
import com.example.mobileapp.ui.screens.contacts.ContactsViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun BioScreen(
    navController: NavController,
    uid: String
) {
    Box(modifier = Modifier.background(color = Color.Cyan).fillMaxSize()) {
        val viewModel = BioViewModel(uid = uid) //passing uid to the viewmodel, so that we get the right users info
        val contacts by viewModel.contacts.collectAsState()
        LazyColumn(
            modifier = Modifier.align(Alignment.Center)
        ) {
            items(contacts) {
                HomeItem(
                    navController,
                    contact = it,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun HomeItem(
    navController: NavController,
    contact: Account,
    viewModel: BioViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    //below are current placeholders for the links
    val igurl = "https://www.instagram.com/oamk_ouas/?hl=fi"
    val ig = Intent(Intent.ACTION_VIEW)
    ig.data = Uri.parse(igurl)
    val twitchurl = "https://www.twitch.tv/"
    val twitch = Intent(Intent.ACTION_VIEW)
    twitch.data = Uri.parse(twitchurl)
    val twitterurl = "https://x.com/oamk_ouas?lang=fi"
    val twitter = Intent(Intent.ACTION_VIEW)
    twitter.data = Uri.parse(twitterurl)
    val youtubeurl = "https://www.youtube.com/@oamk_ouas"
    val youtube = Intent(Intent.ACTION_VIEW)
    youtube.data = Uri.parse(youtubeurl)
    val tiktokurl = "https://www.tiktok.com/@oamk_ouas"
    val tiktok = Intent(Intent.ACTION_VIEW)
    tiktok.data = Uri.parse(tiktokurl)

    val list = mutableListOf("testi", "tagi", "miten", "menee") //placeholder list of tags
    val minPrice: Float = 500.5F
    val maxPrice: Float = 1500.545F
    Box() {
        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.size(width = 350.dp, height = 350.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(50.dp),
                    modifier = Modifier.padding(top = 50.dp, start = 30.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.bioplaceholder),
                        contentDescription = null,
                        modifier = Modifier.size(width = 46.dp, height = 46.dp)
                            .clickable { }
                    )


                    Text(contact.name, fontSize = 32.sp) //here is the username
                }
                Column() {
                    Text(
                        "tags: $list",
                        fontSize = 25.sp,
                        modifier = Modifier.padding(top = 30.dp, start = 50.dp)
                    )
                    Text(
                        "Min price: $minPrice" + "€",
                        fontSize = 25.sp,
                        modifier = Modifier.padding(top = 10.dp,start = 50.dp)
                    )
                    Text(
                        "Max price: $maxPrice" + "€",
                        fontSize = 25.sp,
                        modifier = Modifier.padding(top = 10.dp,start = 50.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f)) // using spacer, so that it doesn't push the images out of the card if the username or the list of users tags is long
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(start = 60.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.instagram),
                        contentDescription = null,
                        modifier = Modifier.size(width = 46.dp, height = 46.dp)
                            .clickable { startActivity(context, ig, null) }
                    )
                    Image(
                        painter = painterResource(R.drawable.twitch),
                        contentDescription = null,
                        modifier = Modifier.size(width = 46.dp, height = 46.dp)
                            .clickable { startActivity(context, twitch, null) }
                    )
                    Image(
                        painter = painterResource(R.drawable.x_everythingapp_logo_twitter),
                        contentDescription = null,
                        modifier = Modifier.size(width = 46.dp, height = 46.dp)
                            .clickable { startActivity(context, twitter, null) }
                    )
                    Image(
                        painter = painterResource(R.drawable.youtube_logo),
                        contentDescription = null,
                        modifier = Modifier.size(width = 46.dp, height = 46.dp)
                            .clickable { startActivity(context, youtube, null) }
                    )
                    Image(
                        painter = painterResource(R.drawable.tiktok),
                        contentDescription = null,
                        modifier = Modifier.size(width = 46.dp, height = 46.dp)
                            .clickable { startActivity(context, tiktok, null) }
                    )
                }
            }


            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.size(width = 350.dp, height = 350.dp)
            ) {

                Text(
                    text = contact.description,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.End).padding(10.dp)
                        .verticalScroll(rememberScrollState())
                )
            }
            Row ( horizontalArrangement = Arrangement.spacedBy(50.dp)){
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(width = 150.dp, height = 46.dp)
                ) {
                    Text("Match")
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(width = 150.dp, height = 46.dp)
                ) {
                    Text("Report")
                }
            }
        }
    }
}