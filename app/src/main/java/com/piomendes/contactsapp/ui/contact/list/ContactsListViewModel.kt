package com.piomendes.contactsapp.ui.contact.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piomendes.contactsapp.data.Contact
import com.piomendes.contactsapp.data.ContactDatasource
import com.piomendes.contactsapp.data.groupByInitial
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ContactsListViewModel : ViewModel() {
    var uiState: ContactsListUiState by mutableStateOf(ContactsListUiState())
        private set


    init {
        loadContacts()
    }

    fun loadContacts() {
        uiState = uiState.copy(isLoading = true, hasError = false)
        viewModelScope.launch {
            delay(1000)
           uiState = try {
               uiState.copy(
                   isLoading = false,
                   contacts = ContactDatasource.instance.findAll().groupByInitial()
               )
           } catch (ex: Exception) {
                Log.e("ContactsListViewModel", "Error loading contacts", ex)
                uiState.copy(isLoading = false, hasError = true)
           }
        }
    }

    fun toggleIsFavorite(contact: Contact) {
        try {
            val updatedContact = contact.copy(isFavorite = !contact.isFavorite)
            ContactDatasource.instance.save(updatedContact)
            uiState = uiState.copy(
                contacts = ContactDatasource.instance.findAll().groupByInitial()
            )
        } catch (ex: Exception) {
            Log.e("ContactsListViewModel", "Error toggling favorite", ex)
        }
    }

}