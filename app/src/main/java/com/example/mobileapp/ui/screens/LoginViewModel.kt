package com.example.mobileapp.ui.screens

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
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

    fun register(email: String, password: String, username: String) {
        _authState.value = AuthState.Loading
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    user?.let {
                        val userData = hashMapOf(
                            "username" to username,
                            "email" to email,
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
                                _authState.value = AuthState.Error(exception.message ?: "something went wrong")
                            }
                    }

                    _authState.value = AuthState.Success(firebaseAuth.currentUser)
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser?) : AuthState()
    data class Error(val message: String) : AuthState()
}