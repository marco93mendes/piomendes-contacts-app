package com.piomendes.contactsapp.ui.contact.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piomendes.contactsapp.data.Contact
import com.piomendes.contactsapp.data.ContactTypeEnum
import com.piomendes.contactsapp.ui.contact.form.composables.FormCheckbox
import com.piomendes.contactsapp.ui.contact.form.composables.FormDatePicker
import com.piomendes.contactsapp.ui.contact.form.composables.FormFieldRow
import com.piomendes.contactsapp.ui.contact.form.composables.FormRadioButton
import com.piomendes.contactsapp.ui.contact.form.composables.FormTextField
import com.piomendes.contactsapp.ui.shared.composables.ContactAvatar
import com.piomendes.contactsapp.ui.shared.composables.DefaultErrorState
import com.piomendes.contactsapp.ui.shared.composables.DefaultLoadingState
import com.piomendes.contactsapp.ui.theme.ContactsAppTheme


// to use on previews
private class BooleanParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}


@Composable
fun ContactFormScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    viewModel: ContactFormViewModel = viewModel()
) {
    val contentModifier: Modifier = modifier.fillMaxSize()

    if (viewModel.uiState.isLoading) {
        DefaultLoadingState(modifier = contentModifier)
    }
    else if (viewModel.uiState.hasErrorLoading) {
        DefaultErrorState (
            modifier = contentModifier,
            onReloadPressed = viewModel::loadContact
        )
    }
    else {
        Scaffold(
            modifier = contentModifier,
            topBar = {
                AppBar(
                    isNewContact = true,
                    onBackPressed = onBackPressed
                )
            },
        ) { paddingValues ->
            FormContent(
                modifier = Modifier.padding(paddingValues),
                contact = viewModel.uiState.contact
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactFormScreenPreview() {
    ContactsAppTheme() {
        ContactFormScreen(onBackPressed = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    modifier: Modifier = Modifier,
    isNewContact: Boolean,
    onBackPressed: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = { Text(if (isNewContact) "New contact" else "Edit contact") },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
//        actions = {
//            IconButton(on) { }
//        }

    )
}


@Preview(showBackground = true)
@Composable
private fun AppBarPreview(
    @PreviewParameter(BooleanParameterProvider::class) isNewContact: Boolean
) {
    ContactsAppTheme() {
        AppBar(isNewContact = isNewContact, onBackPressed = {})
    }
}

@Composable
fun FormContent(
    modifier: Modifier = Modifier,
    contact: Contact
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val formTextFieldModifier: Modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)

        ContactAvatar(
            modifier = Modifier.padding(16.dp),
            firstName = contact.firstName,
            lastName = contact.lastName,
            size = 150.dp,
            textStyle = MaterialTheme.typography.displayLarge
        )

        FormFieldRow(
            label = "First Name",
            imageVector = Icons.Filled.Person,
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "First Name",
                value = contact.firstName,
                onValueChange = {},
                keyboardCapitalization = KeyboardCapitalization.Words

            )
        }

        FormFieldRow(
            label = "Last Name",
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Last Name",
                value = contact.lastName,
                onValueChange = {},
                keyboardCapitalization = KeyboardCapitalization.Words
            )
        }

        FormFieldRow(
            label = "Phone",
            imageVector = Icons.Filled.Phone,
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Phone Number",
                value = contact.phoneNumber,
                onValueChange = {},
                keyboardType = KeyboardType.Phone
            )
        }

        FormFieldRow(
            label = "Email",
            imageVector = Icons.Filled.Mail,
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Email",
                value = contact.email,
                onValueChange = {},
                keyboardType = KeyboardType.Email
            )
        }

        FormFieldRow(
            label = "Birth Date",
        ) {
            FormDatePicker(
                modifier = formTextFieldModifier,
                label = "Birth Date",
                value = contact.birthDate,
                onValueChange = {},
            )
        }

        FormFieldRow(
            label = "Net Worth",
            imageVector = Icons.Filled.AttachMoney,
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Net Worth",
                value = contact.assetValue.toString(),
                onValueChange = {},
                keyboardType = KeyboardType.Decimal
            )
        }

        val choiceOptionModifier = Modifier.padding(8.dp)
        FormFieldRow(
            label = "Favorite",
        ) {
            FormCheckbox(
                modifier = choiceOptionModifier,
                label = "Favorite",
                checked = contact.isFavorite,
                onCheckedChange = {}
            )
        }

        FormFieldRow(
            label = "Type",
        ) {
            FormRadioButton(
                modifier = choiceOptionModifier,
                label = "Personal",
                value = ContactTypeEnum.PERSONAL,
                groupValue = contact.type,
                onValueChanged = {},
            )
            FormRadioButton(
                modifier = choiceOptionModifier,
                label = "Professional",
                value = ContactTypeEnum.PROFESSIONAL,
                groupValue = contact.type,
                onValueChanged = {},
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FormContentPreview() {
    ContactsAppTheme {
        FormContent(
            contact = Contact()
        )
    }
}