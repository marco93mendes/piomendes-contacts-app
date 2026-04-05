package com.piomendes.contactsapp.data

import com.piomendes.contactsapp.data.utils.generateContacts


class ContactDatasource private constructor(){
    companion object {
        val instance: ContactDatasource by lazy {
            ContactDatasource()
        }
    }

    private val contacts: MutableList<Contact> = mutableListOf()

    init {
        contacts.addAll(generateContacts())
    }

    fun findAll(): List<Contact> = contacts.toList()

    fun finById(id: Int): Contact? = contacts.firstOrNull { it.id == id }

    fun save(contact: Contact): Contact {
        if (contact.id > 0) {
            //update
            val index: Int = contacts.indexOfFirst { it.id == contact.id }
            contacts[index] = contact
            return contact
        } else {
            //insert
            val maxId: Int = contacts.maxBy { it.id }.id
            val newContact = contact.copy(id = maxId + 1)
            contacts.add(newContact)
            return newContact
        }
    }

    fun delete(contact: Contact) {
        if (contact.id > 0) {
            contacts.removeIf { it.id == contact.id } //remove all with this ID
        }
    }

}