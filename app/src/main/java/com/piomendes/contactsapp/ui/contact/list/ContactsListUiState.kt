package com.piomendes.contactsapp.ui.contact.list

import com.piomendes.contactsapp.data.Contact

data class ContactsListUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val contacts: List<Contact> = emptyList()
)
