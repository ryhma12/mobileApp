package com.example.mobileapp.ui.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mobileapp.R.drawable.bioplaceholder
import com.example.mobileapp.model.Contact

class ContactsViewModel: ViewModel() {
    private val _contacts = mutableStateOf(emptyList<Contact>())
    val contacts: State<List<Contact>> = _contacts

    init {
        loadContacts()
    }

    private fun loadContacts() {
        _contacts.value = listOf(
            Contact(1, bioplaceholder, "qwe rqwerqw"),
            Contact(2, bioplaceholder, "eqw rqqew"),
            Contact(3, bioplaceholder, "wqeqwe qwerqw"),
        )
    }
}