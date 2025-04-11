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
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(navController: NavController) {
    Box(modifier = Modifier.background(color = Color.Cyan).fillMaxSize()) {
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

        val list = mutableListOf("testi", "tagi", "miten", "menee")

        Box(Modifier.align(Alignment.Center)) {
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
                                .clickable {  }
                        )
                        Text("Veikko Vaikuttaja", fontSize = 32.sp)
                    }
                    Text("tags: $list", fontSize = 25.sp, modifier = Modifier.padding(top = 70.dp, start = 50.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 50.dp, start = 60.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.instagram),
                            contentDescription = null,
                            modifier = Modifier.size(width = 46.dp, height = 46.dp)
                                .clickable { startActivity(context,ig,null) }
                        )
                        Image(
                            painter = painterResource(R.drawable.twitch),
                            contentDescription = null,
                            modifier = Modifier.size(width = 46.dp, height = 46.dp)
                                .clickable { startActivity(context,twitch,null)  }
                        )
                        Image(
                            painter = painterResource(R.drawable.x_everythingapp_logo_twitter),
                            contentDescription = null,
                            modifier = Modifier.size(width = 46.dp, height = 46.dp)
                                .clickable {  startActivity(context,twitter,null) }
                        )
                        Image(
                            painter = painterResource(R.drawable.youtube_logo),
                            contentDescription = null,
                            modifier = Modifier.size(width = 46.dp, height = 46.dp)
                                .clickable {  startActivity(context,youtube,null) }
                        )
                        Image(
                            painter = painterResource(R.drawable.tiktok),
                            contentDescription = null,
                            modifier = Modifier.size(width = 46.dp, height = 46.dp)
                                .clickable {  startActivity(context,tiktok,null) }
                        )
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.size(width = 350.dp, height = 350.dp)
                ) {
                    Text(
                        text = stringResource(R.string.bio),
                        fontSize = 24.sp,
                        modifier = Modifier.align(Alignment.End).padding(10.dp).verticalScroll(rememberScrollState())
                    )
                }
            }
        }
    }
}

/*


*var builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)


*var builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle("My notification")
        .setContentText("Much longer text that cannot fit one line...")
        .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)


      private fun createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


* // Create an explicit intent for an Activity in your app.
val intent = Intent(this, AlertDetails::class.java).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
}
val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle("My notification")
        .setContentText("Hello World!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // Set the intent that fires when the user taps the notification.
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)


        * with(NotificationManagerCompat.from(this)) {
    if (ActivityCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO: Consider calling
        // ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        //                                        grantResults: IntArray)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.

        return@with
    }
    // notificationId is a unique int for each notification that you must define.
    notify(NOTIFICATION_ID, builder.build())
}
* */