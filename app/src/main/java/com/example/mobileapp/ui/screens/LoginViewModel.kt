package com.example.mobileapp.ui.screens

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import com.example.mobileapp.R
import com.example.mobileapp.model.Account
import com.example.mobileapp.model.GoogleAccount
import com.example.mobileapp.model.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class LoginViewModel(
    private val context: Context,
    private val oneTapClient: SignInClient
) : ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    _authState.value = AuthState.Success(firebaseAuth.currentUser)
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "something went wrong")
                }
            }
    }

    fun register(email: String, password: String,verifyPassword: String ,username: String, accountType: String) {
        _authState.value = AuthState.Loading
        if(password != verifyPassword){
            _authState.value = AuthState.Error("passwords do not match")
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.let {
                        val userData = hashMapOf(
                            "username" to username,
                            "email" to email,
                            "uid" to it.uid,
                            "type" to accountType
                        )

                        val firestore = FirebaseFirestore.getInstance()
                        firestore.collection("users")
                            .document(it.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                _authState.value = AuthState.Success(user)
                            }
                            .addOnFailureListener { exception ->
                                _authState.value = AuthState.Error(exception.message ?: "something went wrong")
                            }
                    }
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    //google sign-in

    private val auth = Firebase.auth

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

    suspend fun signInWithIntent(intent: Intent): UserData? {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            user?.let {
                val userData = hashMapOf(
                    "username" to it.displayName,
                    "email" to it.email,
                    "uid" to it.uid,
                )

                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("users")
                    .document(it.uid)
                    .set(userData)
                    .addOnSuccessListener {
                        _authState.value = AuthState.Success(user)
                    }
                    .addOnFailureListener { exception ->
                        _authState.value = AuthState.Error(exception.message ?: "Something went wrong")
                    }

                UserData(
                    userId = it.uid,
                    username = it.displayName
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            _authState.value = AuthState.Error(e.message ?: "Something went wrong")
            null
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
            _authState.value = AuthState.Idle
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
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

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser?) : AuthState()
    data class Error(val message: String) : AuthState()
}