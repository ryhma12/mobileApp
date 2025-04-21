package com.example.mobileapp.ui.screens.contract

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.mobileapp.model.Account
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await


class CreateContractViewModel(selectedContactUid: String) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val savedStateHandle = extras.createSavedStateHandle()

                val selectedContactUid = savedStateHandle.get<String>("selectedContactUid")!!
                return CreateContractViewModel(selectedContactUid) as T
            }
        }
    }

    private val firestore = FirebaseFirestore.getInstance()
    var user: Account? = null

    init {
        fetchUserByUid(selectedContactUid)
    }

    private fun fetchUserByUid(uid: String) {
        viewModelScope.launch {
            try {
                val userDocument = firestore.collection("users").document(uid).get().await()
                if (userDocument.exists()) {
                    user = userDocument.toObject<Account>()
                } else {
                    Log.d("404", "user not found")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

