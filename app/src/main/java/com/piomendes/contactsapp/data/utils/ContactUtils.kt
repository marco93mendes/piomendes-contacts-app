package com.piomendes.contactsapp.data.utils

import com.piomendes.contactsapp.data.Contact
import kotlin.random.Random


fun generateContacts(): List<Contact> {
    val contacts = mutableListOf<Contact>()
    val firstNames = listOf("Ana", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda", "Gabriel", "Helena", "Igor", "Juliana")
    val lastNames = listOf("Almeida", "Barbosa", "Cardoso", "Dias", "Esteves", "Ferreira", "Gomes", "Henriques", "Ibrahim", "Jardim")
    for (i in 0 until 20) {
        var firstName: String
        var lastName: String
        var fullName: String
        do {
            firstName = firstNames.random()
            lastName = lastNames.random()
            fullName = "$firstName $lastName"
        } while (contacts.any { it.fullName == fullName })
        val email = "${fullName.lowercase()}@mail.com"
        val phoneNumber = buildString {
            append("(XX) 9")
            repeat(8) { append(Random.nextInt(0, 10)) }
        }
        val newContact = Contact(
            id = i + 1,
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