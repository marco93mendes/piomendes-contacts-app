package com.piomendes.contactsapp.ui.contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.piomendes.contactsapp.R
import com.piomendes.contactsapp.data.Contact
import com.piomendes.contactsapp.ui.theme.ContactsAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun ContactsListScreen(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val isInitialCompositionState: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) }
    val isLoadingState: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
    val isErrorState: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) }
    val contactsState: MutableState<List<Contact>> = rememberSaveable { mutableStateOf(listOf()) }

    val loadContacts: () -> Unit = {
        isLoadingState.value = true
        isErrorState.value = false

        coroutineScope.launch {
            delay(2000)
            isErrorState.value = Random.nextBoolean()
            if (!isErrorState.value) {
                val isEmpty = false//Random.nextBoolean()
                if (isEmpty) {
                   contactsState.value = listOf()
                } else {
                    contactsState.value = generateContacts()
                }
            }
            isLoadingState.value = false
        }
    }
    
    val toggleIsFavorite: (Contact) -> Unit = { favoritedContact ->
        contactsState.value = contactsState.value.map { currentContact ->
            if (currentContact.id == favoritedContact.id) {
                currentContact.copy(isFavorite = !currentContact.isFavorite)
            } else {
                currentContact
            }
        }
    }

    if (isInitialCompositionState.value) {
        loadContacts()
        isInitialCompositionState.value = false
    }

    val contentModifier = modifier.fillMaxSize()
    if (isLoadingState.value) {
        LoadingState(modifier = contentModifier)
    } else if (isErrorState.value) {
        ErrorState(modifier = contentModifier, onReloadPressed = loadContacts)
    } else {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                AppBar(
                    onRefreshPressed = loadContacts
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = {
                    contactsState.value = contactsState.value.plus(
                        Contact(id = contactsState.value.size + 1, "${contactsState.value.size} NEW", "CONTACT")
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add_contact_description)
                    )
                    Spacer(Modifier.size(8.dp))
                    Text("New contact")
                }
            }
        ) { paddingValues ->
            val defaultModifier: Modifier = Modifier.padding(paddingValues)
            if (contactsState.value.isEmpty()) {
                EmptyList(modifier = defaultModifier)
            } else {
                List(
                    modifier = defaultModifier,
                    contacts = contactsState.value,
                    onFavoritePressed = toggleIsFavorite
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    onRefreshPressed: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.contacts_title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = onRefreshPressed) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.refresh_button)
                )
            }
        }

    )
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(60.dp)
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.loading_contacts),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    onReloadPressed: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Filled.CloudOff,
            contentDescription = stringResource(R.string.error_icon_description),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )

        val textPadding = PaddingValues(top = 8.dp, start = 8.dp, end = 8.dp)

        Text(
            modifier = Modifier.padding(textPadding),
            text = stringResource(R.string.error_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(textPadding),
            text = stringResource(R.string.error_subtitle),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        ElevatedButton(
            modifier = Modifier.padding(top = 48.dp),
            onClick = onReloadPressed
        ) {
            Text(stringResource(R.string.reload_button))
        }
    }
}

@Composable
fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.no_data),
            contentDescription = stringResource(R.string.empty_icon_description),
        )
        Text(
            text = stringResource(R.string.empty_title),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.empty_subtitle),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@Composable
fun List(
    modifier: Modifier = Modifier,
    contacts: List<Contact> = emptyList(),
    onFavoritePressed: (Contact) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(contacts) { contact ->
            ContactListItem(contact = contact, onFavoritePressed = onFavoritePressed)
        }
    }
}

@Composable
fun ContactListItem(
    modifier: Modifier = Modifier,
    contact: Contact,
    onFavoritePressed: (Contact) -> Unit
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(contact.fullName)
        },
        trailingContent = {
            IconButton(
                onClick = {
                    onFavoritePressed(contact)
                }
            ) {
                if (contact.isFavorite) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = stringResource(R.string.favorite_icon_description),
                        tint = Color.Red
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = stringResource(R.string.favorite_icon_description),
                        tint = LocalContentColor.current
                    )
                }
            }
        }
    )
}

///////////////////////////////////////
////////////// PREVIEWS ///////////////
///////////////////////////////////////

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    ContactsAppTheme {
        AppBar(
            onRefreshPressed = {}
        )
    }
}

@Preview(showBackground = true, heightDp = 120)
@Composable
fun LoadingStatePreview() {
    ContactsAppTheme {
        LoadingState()
    }
}

@Preview(showBackground = true, heightDp = 500)
@Composable
fun ErrorStatePreview() {
    ContactsAppTheme {
        ErrorState(
            onReloadPressed = {}
        )
    }
}

@Preview(showBackground = true, heightDp = 450)
@Composable
fun EmptyListPreview() {
    ContactsAppTheme {
        EmptyList()
    }
}

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    ContactsAppTheme() {
        List(contacts = generateContacts(), onFavoritePressed = {})
    }
}

private fun generateContacts(): List<Contact> {
    val firstNames = listOf("Ana", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda", "Gabriel", "Helena", "Igor", "Juliana")
    val lastNames = listOf("Almeida", "Barbosa", "Cardoso", "Dias", "Esteves", "Ferreira", "Gomes", "Henriques", "Ibrahim", "Jardim")
    val contacts: MutableList<Contact> = mutableListOf()
    for (i in 0 until 15) {
        var generatedNewContact = false
        while (!generatedNewContact) {
            val firstNameIndex = Random.nextInt(firstNames.size)
            val lastNameIndex = Random.nextInt(lastNames.size)
            val firstName = firstNames[firstNameIndex]
            val lastName = lastNames[lastNameIndex]
            val fullName = "$firstName $lastName"
            val phoneNumber = buildString {
                append("(XX) 9")
                repeat(4) { append(Random.nextInt(0, 10)) }
                append("-")
                repeat(4) { append(Random.nextInt(0, 10)) }
            }
            val email = "${firstName.lowercase()}.${lastName.lowercase()}@gmail.com"
            val newContact = Contact(id = i + 1, firstName = firstName, lastName = lastName, phoneNumber = phoneNumber, email = email, isFavorite = Random.nextBoolean())
            if (contacts.none { it.fullName == fullName }) {
                contacts.add(newContact)
                generatedNewContact = true
            }
        }
    }
    return contacts
}