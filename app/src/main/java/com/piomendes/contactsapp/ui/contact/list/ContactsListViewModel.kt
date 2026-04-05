package com.piomendes.contactsapp.ui.contact.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piomendes.contactsapp.data.Contact
import com.piomendes.contactsapp.data.groupByInitial
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
                val isEmpty = Random.nextBoolean() //TODO
                if (isEmpty)
                    uiState.value = uiState.value.copy(isLoading = false, contacts = emptyMap() )
                else
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        contacts = generateContacts().groupByInitial()
                    )
            }
        }
    }

    fun toggleIsFavorite(updatedContact: Contact) {
        val newMap: MutableMap<String, List<Contact>> = mutableMapOf()
        uiState.value.contacts.forEach { (key, listContacts) ->
            newMap[key] = listContacts.map { currentContact ->
                if (currentContact.id == updatedContact.id) {
                    currentContact.copy(isFavorite = !currentContact.isFavorite)
                } else {
                    currentContact
                }
            }
        }
        uiState.value = uiState.value.copy(contacts = newMap)
    }

}