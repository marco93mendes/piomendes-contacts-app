package com.piomendes.contactsapp.data

data class Contact(
    val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String = "",
    val email: String = "",
    val isFavorite: Boolean = false
) {
    val fullName get() = "$firstName $lastName".trim()
}

fun List<Contact>.groupByInitial() : Map<String, List<Contact>> {
    return sortedBy { it.fullName } .groupBy { it.fullName.take(1) }
}