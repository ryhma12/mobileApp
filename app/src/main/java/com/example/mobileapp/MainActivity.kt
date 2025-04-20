package com.example.mobileapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.mobileapp.ui.screens.LoginViewModel

import com.example.mobileapp.ui.theme.MobileAppTheme
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var loginViewModel: LoginViewModel

    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val intent = result.data ?: return@launch
                    val account = loginViewModel.signInWithIntent(intent)
                    if (account?.username != null) {
                        Log.d("SIGN_IN", "Success: ${account.username}")
                    } else {
                        Log.e("SIGN_IN", "Error")
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneTapClient = Identity.getSignInClient(this)
        loginViewModel = LoginViewModel(this, oneTapClient)

        enableEdgeToEdge()
        setContent {
            MobileAppTheme {
               MobileApp(
                   viewModel = loginViewModel,
                   onSignInClick = {
                       lifecycleScope.launch {
                           val intentSender = loginViewModel.signIn()
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
