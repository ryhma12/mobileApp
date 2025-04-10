package com.example.mobileapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.model.Account
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val allUsers = MutableStateFlow<List<Account>>(emptyList())
    private val _filteredUsers = MutableStateFlow<List<Account>>(emptyList())
    val filteredUsers: StateFlow<List<Account>> = _filteredUsers

    private val firestore = FirebaseFirestore.getInstance()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            firestore.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    val userList = result.documents.mapNotNull { doc ->
                        doc.toObject(Account::class.java)
                    }
                    allUsers.value = userList
                    _filteredUsers.value = userList
                }
                .addOnFailureListener { exception ->
                    //handle errors
                }
        }
    }

    fun onQueryChange(query: String) {
        if(query.isBlank()) {
            _filteredUsers.value = allUsers.value
        }else {
            _filteredUsers.value = allUsers.value.filter {
                it.username.contains(query, ignoreCase = true)
            }
        }
    }
}