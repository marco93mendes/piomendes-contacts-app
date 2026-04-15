package com.piomendes.contactsapp.ui.contact.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piomendes.contactsapp.data.ContactDatasource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ContactFormViwModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val contactId: Int = savedStateHandle.get<String>("id")?.toIntOrNull() ?: 0

    var uiState: ContactFormState by mutableStateOf(
        ContactFormState(contactId = contactId)
    )


    init {
        if (!uiState.isNewContact) {
            loadContact()
        }
    }

    private fun loadContact() {
        uiState = uiState.copy(isLoading = true, hasErrorLoading = false)

        viewModelScope.launch {
            delay(2000)
            val contact = ContactDatasource.instance.findById(contactId)
            val hasError = Random.nextBoolean()
            uiState = if (contact == null || hasError) {
                uiState.copy(isLoading = false, hasErrorLoading = true)
            } else {
                uiState.copy(isLoading = false, contact = contact)
            }
        }

    }


}