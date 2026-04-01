package com.piomendes.contactsapp.ui.contact.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piomendes.contactsapp.data.Contact
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ContactsListViewModel : ViewModel() {
    val uiState: MutableState<ContactsListUiState> = mutableStateOf(ContactsListUiState())

    init {
        loadContacts()
    }

    fun loadContacts() {
        uiState.value = uiState.value.copy(isLoading = true, hasError = false)

        viewModelScope.launch {
            delay(2000)
            val hasError = Random.nextBoolean()

            if (hasError) {
                uiState.value = uiState.value.copy(isLoading = false, hasError = true)
            }
            else {
                val isEmpty = Random.nextBoolean()
                if (isEmpty)
                    uiState.value = uiState.value.copy(isLoading = false, contacts = emptyList(), )
                else
                    uiState.value = uiState.value.copy(isLoading = false, contacts = generateContacts())
            }
        }
    }

    fun toggleIsFavorite(updated: Contact) {
        uiState.value = uiState.value.copy(
            contacts = uiState.value.contacts.map { current ->
                if (current.id == updated.id) {
                    current.copy(isFavorite = !current.isFavorite)
                } else {
                    current
                }
            }
        )
    }

}