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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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

@Composable
fun ContactsListScreen(modifier: Modifier = Modifier) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = { Text(stringResource(R.string.contacts_title)) }
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
fun ErrorState(modifier: Modifier = Modifier) {
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
            modifier = Modifier.padding(top = 16.dp),
            onClick = { /*TODO*/ }
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
fun List(modifier: Modifier = Modifier, contacts: List<Contact> = emptyList()) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
    ) {
        contacts.forEach { contact ->
            var isFavorite = contact.isFavorite
            ListItem(
                headlineContent = {
                    Text(contact.fullName)
                },
                trailingContent = {
                    IconButton(
                        onClick = {
                            isFavorite = !isFavorite
                        }
                    ) {
                        if (isFavorite) {
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
    }
}

///////////////////////////////////////
////////////// PREVIEWS ///////////////
///////////////////////////////////////

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    ContactsAppTheme {
        AppBar()
    }
}

@Preview(showBackground = true, heightDp = 120)
@Composable
fun LoadingStatePreview() {
    ContactsAppTheme {
        LoadingState()
    }
}

@Preview(showBackground = true, heightDp = 220)
@Composable
fun ErrorStatePreview() {
    ContactsAppTheme {
        ErrorState()
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
        List(
            contacts = listOf (
                Contact(1, "Alice", "Almeida", "4191234-5678", "alice.almeida@gmail.com", false),
                Contact(2, "Bruno", "Barbosa", "4199157-4812", "bruno.barbosa@gmail.com", true),
                Contact(3, "Carlos", "Cardoso", "4198765-4321", "carlos.cardoso@gmail.com", true),
                Contact(4, "Daniel", "Dias", "4199988-7755", "daniel.dias@gmail.com", false),
                Contact(5, "Eduardo", "Esteves", "4199123-1111", "eduardo.esteves@gmail.com", false),
                Contact(6, "Fernanda", "Ferreira", "4199345-2222", "fernanda.ferreira@gmail.com", true),
                Contact(7, "Gabriel", "Gomes", "4199567-3333", "gabriel.gomes@gmail.com", false),
                Contact(8, "Helena", "Holanda", "4199789-4444", "helena.henrique@gmail.com", true),
                Contact(9, "Igor", "Ibrahim", "4199012-5555", "igor.ibrahim@gmail.com", false),
                Contact(10, "Juliana", "Jardim", "4199234-6666", "juliana.jardim@gmail.com", true),
                Contact(11, "Kaio", "Klein", "4199456-7777", "kaio.klein@gmail.com", false),
                Contact(12, "Larissa", "Lima", "4199678-8888", "larissa.lima@gmail.com", true),
                Contact(13, "Marco", "Mendes", "4199890-9999", "marcos.mendes@gmail.com", false),
                Contact(14, "Natália", "Nogueira", "4199001-1010", "natalia.nogueira@gmail.com", true),
                Contact(15, "Otávio", "Oliveira", "4199222-2020", "otavio.oliveira@gmail.com", false),
                Contact(16, "Paula", "Pereira", "4199333-3030", "paula.pereira@gmail.com", true),
                Contact(17, "Quésia", "Queiroz", "4199444-4040", "quesia.queiroz@gmail.com", false),
                Contact(18, "Rafael", "Ribeiro", "4199555-5050", "rafael.ribeiro@gmail.com", true),
                Contact(19, "Sabrina", "Silva", "4199666-6060", "sabrina.silva@gmail.com", false),
                Contact(20, "Tiago", "Teixeira", "4199777-7070", "tiago.teixeira@gmail.com", true)            )
        )
    }
}