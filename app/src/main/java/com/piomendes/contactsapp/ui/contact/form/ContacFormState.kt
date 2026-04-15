package com.piomendes.contactsapp.ui.contact.form

import com.piomendes.contactsapp.data.Contact

data class ContactFormState(
    val contactId: Int = 0,
    val isLoading: Boolean = false,
    val contact: Contact = Contact(),
    val hasErrorLoading: Boolean = false
) {
    val isNewContact: Boolean
        get() = contactId <= 0
}