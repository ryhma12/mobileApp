import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import com.example.mobileapp.R
import com.example.mobileapp.model.GoogleAccount
import com.example.mobileapp.model.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class SettingsViewModel(
    private val context: Context,
    private val oneTapClient: SignInClient
) : ViewModel() {
    private val auth = Firebase.auth
    private val _state = MutableStateFlow(googleSignInState())
    val state = _state.asStateFlow()

    val currentUser = auth.currentUser

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    fun isLinked(): Boolean {
        return currentUser?.providerData?.any { it.providerId == "google.com" } == true
    }

    suspend fun signInWithIntent(intent: Intent): GoogleAccount {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            GoogleAccount(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            GoogleAccount(data = null, errorMessage = e.message)
        }
    }

    suspend fun startGoogleAccountLink(): IntentSender? {
        return try {
            val result = oneTapClient.beginSignIn(buildSignInRequest()).await()
            result.pendingIntent.intentSender
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
    }

    suspend fun linkGoogleAccountFromIntent(intent: Intent): String? {
        val currentUser = auth.currentUser ?: return "No user signed in"

        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val googleIdToken = credential.googleIdToken ?: return "No Google ID token"
            val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

            currentUser.linkWithCredential(googleCredential).await()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            e.message
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}

data class googleSignInState(
    val signInSuccesfull: Boolean = false
)
