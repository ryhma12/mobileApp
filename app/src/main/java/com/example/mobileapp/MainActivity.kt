package com.example.mobileapp

import SettingsViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.example.mobileapp.ui.screens.HomeScreen

import com.example.mobileapp.ui.theme.MobileAppTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var settingsViewModel: SettingsViewModel

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val intent = result.data ?: return@launch
                    val account = settingsViewModel.signInWithIntent(intent)
                    if (account.data != null) {
                        Log.d("SIGN_IN", "Success: ${account.data.username}")
                    } else {
                        Log.e("SIGN_IN", "Error: ${account.errorMessage}")
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneTapClient = Identity.getSignInClient(this)
        settingsViewModel = SettingsViewModel(this, oneTapClient)

        enableEdgeToEdge()
        setContent {
            MobileAppTheme {
               MobileApp(
                   viewModel = settingsViewModel,
                   onSignInClick = {
                       lifecycleScope.launch {
                           val intentSender = settingsViewModel.signIn()
                           intentSender?.let {
                               signInLauncher.launch(
                                   IntentSenderRequest.Builder(it).build()
                               )
                           }
                       }
                   }
               )
            }
        }
    }
}


/*
@Preview
@Composable
fun MobileAppPreview() {
    MobileAppTheme {
        MobileApp()
    }
}
*/
