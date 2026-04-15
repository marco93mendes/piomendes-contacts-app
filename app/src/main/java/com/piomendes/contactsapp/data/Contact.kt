package com.piomendes.contactsapp.data

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class ContactTypeEnum {
    PERSONAL,
    PROFESSIONAL
}

data class Contact(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val isFavorite: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val birthDate: LocalDate = LocalDate.now(),
    val type: ContactTypeEnum = ContactTypeEnum.PERSONAL,
    val assetValue: BigDecimal = BigDecimal.ZERO
) {
    val fullName get() = "$firstName $lastName".trim()
}


fun List<Contact>.groupByInitial() : Map<String, List<Contact>> {
    return sortedBy { it.fullName } .groupBy { it.fullName.take(1) }
}

fun LocalDate.format(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return format(formatter)
}