package com.piomendes.contactsapp.data.utils

import com.piomendes.contactsapp.data.Contact
import kotlin.random.Random


fun generateContacts(): List<Contact> {
    val contacts = mutableListOf<Contact>()
    val firstNames = listOf("Arnold", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda", "Gabriel", "Helena", "Igor", "Juliana")
    val lastNames = listOf("Almeida", "Barbosa", "Cardoso", "Dias", "Esteves", "Ferreira", "Gomes", "Henriques", "Ibrahim", "Jardim")
    for (i in 1 .. 20) {
        var firstName = firstNames.random()
        var lastName = lastNames.random()
        var fullName = "$firstName $lastName"
        while (contacts.any { it.fullName == fullName }) {
            firstName = firstNames.random()
            lastName = lastNames.random()
            fullName = "$firstName $lastName"
        }
        val email = "${fullName.lowercase()}@mail.com"
        val phoneNumber = "(XX) 9 ${Random.nextInt(1000, 9999)}-${Random.nextInt(1000, 9999)}"
        val newContact = Contact(
            id = i,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            email = email,
            isFavorite = Random.nextBoolean()
        )
        contacts.add(newContact)
    }
    return contacts
}