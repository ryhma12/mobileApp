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
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import com.example.mobileapp.MainActivity
import com.example.mobileapp.R

val CHANNEL_ID="ChannelId"
val CHANNEL_NAME="ChannelName"
val NOTIF_ID=0


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(navController: NavController) {
    Box(modifier = Modifier.background(color = Color.Cyan).fillMaxSize()) {

        val context = LocalContext.current
        val notifManager = NotificationManagerCompat.from(context)

        // Create the notification channel
        createNotifyChannel()

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Sample title")
            .setContentText("This is a sample body notification")
            .setSmallIcon(R.drawable.bioplaceholder)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

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
                        )
                        Text("Veikko Vaikuttaja", fontSize = 32.sp)
                    }

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 200.dp, start = 60.dp)
                    ) {
                        Button(
                            onClick = {
                                // Check for the notification permission before showing it
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    notifManager.notify(NOTIF_ID, notif)
                                } else {
                                    ActivityCompat.requestPermissions(
                                        context as Activity,
                                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                        1
                                    )
                                }
                            },
                            modifier = Modifier.size(width = 46.dp, height = 46.dp)
                        ) {
                            Text("X", fontSize = 5.sp)
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier.size(width = 46.dp, height = 46.dp)) {
                            Text("Twitch")
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier.size(width = 46.dp, height = 46.dp)) {
                            Text("YouTube")
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier.size(width = 46.dp, height = 46.dp)) {
                            Text("Instagram")
                        }
                        Button(onClick = { /*TODO*/ }, modifier = Modifier.size(width = 46.dp, height = 46.dp)) {
                            Text("TikTok")
                        }
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

@Composable
private fun createNotifyChannel() {
    val context = LocalContext.current

    // Create notification channel for API >= 26 (Oreo)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            enableLights(true)
        }

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
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