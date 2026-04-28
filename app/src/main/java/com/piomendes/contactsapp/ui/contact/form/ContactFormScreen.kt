package com.piomendes.contactsapp.ui.contact.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piomendes.contactsapp.data.ContactTypeEnum
import com.piomendes.contactsapp.ui.contact.form.composables.FormCheckbox
import com.piomendes.contactsapp.ui.contact.form.composables.FormDatePicker
import com.piomendes.contactsapp.ui.contact.form.composables.FormFieldRow
import com.piomendes.contactsapp.ui.contact.form.composables.FormRadioButton
import com.piomendes.contactsapp.ui.contact.form.composables.FormTextField
import com.piomendes.contactsapp.ui.contact.form.visualtransformation.CurrencyVisualTransformation
import com.piomendes.contactsapp.ui.contact.form.visualtransformation.PhoneVisualTransformation
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
    viewModel: ContactFormViewModel = viewModel(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onUpdated: () -> Unit
) {

    LaunchedEffect(viewModel.uiState.contactUpdated) {
        if (viewModel.uiState.contactUpdated) {
            onUpdated()
        }
    }

    LaunchedEffect(snackBarHostState, viewModel.uiState.processingErrorMessage) {
        if (viewModel.uiState.processingErrorMessage.isNotBlank()) {
            snackBarHostState.showSnackbar(viewModel.uiState.processingErrorMessage)
        }
    }

    if (viewModel.uiState.showConfirmationDialog) {
        ConfirmationDialog(
            content = "Delete this contact permanently?",
            onDismiss = viewModel::hideConfirmationDialog,
            onConfirm = viewModel::delete
        )
    }


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
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            },
            topBar = {
                AppBar(
                    isNewContact = viewModel.uiState.isNewContact,
                    onBackPressed = onBackPressed,
                    isProcessing = viewModel.uiState.isProcessing,
                    onSavePressed = viewModel::save,
                    onDeletePressed = viewModel::showConfirmationDialog
                )
            },
        ) { paddingValues ->
            FormContent(
                modifier = Modifier.padding(paddingValues),
                formState = viewModel.uiState.formState,
                onFormEvent = viewModel::onFormEvent,
                isSaving = viewModel.uiState.isProcessing
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContactFormScreenPreview() {
    ContactsAppTheme() {
        ContactFormScreen(
            onBackPressed = {},
            onUpdated = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    modifier: Modifier = Modifier,
    isNewContact: Boolean,
    onBackPressed: () -> Unit = {},
    isProcessing: Boolean,
    onSavePressed: () -> Unit,
    onDeletePressed: () -> Unit
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
        },
        actions = {
            if (isProcessing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(16.dp),
                    strokeWidth = 2.dp
                )
            } else {
                if (!isNewContact) {
                    IconButton(onClick = onDeletePressed) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
                IconButton(onClick = onSavePressed) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Save"
                    )
                }
            }
        }

    )
}


@Preview(showBackground = true)
@Composable
private fun AppBarPreview(
    @PreviewParameter(BooleanParameterProvider::class) isNewContact: Boolean
) {
    ContactsAppTheme() {
        AppBar(
            isNewContact = isNewContact,
            onBackPressed = {},
            isProcessing = false,
            onSavePressed = {},
            onDeletePressed = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppBarPreviewSaving(
    @PreviewParameter(BooleanParameterProvider::class) isSaving: Boolean
) {
    ContactsAppTheme() {
        AppBar(
            isNewContact = true,
            onBackPressed = {},
            isProcessing = isSaving,
            onSavePressed = {},
            onDeletePressed = {}
        )
    }
}


@Composable
fun FormContent(
    modifier: Modifier = Modifier,
    formState: FormState,
    onFormEvent: (FormEvent) -> Unit,
    isSaving: Boolean
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val formTextFieldModifier: Modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)

        ContactAvatar(
            modifier = Modifier.padding(16.dp),
            firstName = formState.firstName.value,
            lastName = formState.lastName.value,
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
                value = formState.firstName.value,
                onValueChange = { newValue ->
                    onFormEvent(FormEvent.UpdateFirstName(newValue))
                },
                errorMessage = formState.firstName.errorMessage,
                keyboardCapitalization = KeyboardCapitalization.Words,
                enabled = !isSaving
            )
        }

        FormFieldRow(
            label = "Last Name",
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Last Name",
                value = formState.lastName.value,
                onValueChange = { newValue ->
                    onFormEvent(FormEvent.UpdateLastName(newValue))
                },
                errorMessage = formState.lastName.errorMessage,
                keyboardCapitalization = KeyboardCapitalization.Words,
                enabled = !isSaving
            )
        }

        FormFieldRow(
            label = "Phone",
            imageVector = Icons.Filled.Phone,
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Phone Number",
                value = formState.phoneNumber.value,
                onValueChange = { newValue ->
                    onFormEvent(FormEvent.UpdatePhoneNumber(newValue))
                },
                errorMessage = formState.phoneNumber.errorMessage,
                keyboardType = KeyboardType.Phone,
                enabled = !isSaving,
                visualTransformation = PhoneVisualTransformation()
            )
        }

        FormFieldRow(
            label = "Email",
            imageVector = Icons.Filled.Mail,
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Email",
                value = formState.email.value,
                onValueChange = { newValue ->
                    onFormEvent(FormEvent.UpdateEmail(newValue))
                },
                errorMessage = formState.email.errorMessage,
                keyboardType = KeyboardType.Email,
                enabled = !isSaving
            )
        }

        FormFieldRow(
            label = "Birth Date",
        ) {
            FormDatePicker(
                modifier = formTextFieldModifier,
                label = "Birth Date",
                value = formState.birthDate.value,
                onValueChange = { newValue ->
                    onFormEvent(FormEvent.UpdateBirthDate(newValue))
                },
                errorMessage = formState.birthDate.errorMessage,
                enabled = !isSaving
            )
        }

        FormFieldRow(
            label = "Net Worth",
            imageVector = Icons.Filled.AttachMoney,
        ) {
            FormTextField(
                modifier = formTextFieldModifier,
                label = "Net Worth",
                value = formState.assetValue.value,
                onValueChange = { newValue ->
                    onFormEvent(FormEvent.UpdateAssetValue(newValue))
                },
                errorMessage = formState.assetValue.errorMessage,
                keyboardType = KeyboardType.Decimal,
                enabled = !isSaving,
                visualTransformation = CurrencyVisualTransformation()
            )
        }

        val choiceOptionModifier = Modifier.padding(8.dp)
        FormFieldRow(
            label = "Favorite",
        ) {
            FormCheckbox(
                modifier = choiceOptionModifier,
                label = "Favorite",
                checked = formState.isFavorite.value,
                onCheckedChange = { newValue ->
                    onFormEvent(FormEvent.UpdateIsFavorite(newValue))
                },
                enabled = !isSaving
            )
        }

        FormFieldRow(
            label = "Type",
        ) {
            FormRadioButton(
                modifier = choiceOptionModifier,
                label = "Personal",
                value = ContactTypeEnum.PERSONAL,
                groupValue = formState.type.value,
                onValueChanged = { newValue ->
                    onFormEvent(FormEvent.UpdateType(newValue))
                },
                enabled = !isSaving
            )
            FormRadioButton(
                modifier = choiceOptionModifier,
                label = "Professional",
                value = ContactTypeEnum.PROFESSIONAL,
                groupValue = formState.type.value,
                onValueChanged = { newValue ->
                    onFormEvent(FormEvent.UpdateType(newValue))
                },
                enabled = !isSaving
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FormContentPreview() {
    ContactsAppTheme {
        FormContent(
            formState = FormState(),
            onFormEvent = {},
            isSaving = false
        )
    }
}

@Composable
fun ConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        title = title?.let { { Text(text = it) } },
        text = { Text(text = content) },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }

    )
}