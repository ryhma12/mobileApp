package com.example.mobileapp.ui.screens

import androidx.lifecycle.ViewModel
import com.example.mobileapp.R.drawable.bioplaceholder
import com.example.mobileapp.model.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ContactsViewModel: ViewModel() {
    private val _contacts = MutableStateFlow(emptyList<Contact>())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

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