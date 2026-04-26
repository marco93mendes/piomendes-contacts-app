package com.piomendes.contactsapp.ui.contact.form

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piomendes.contactsapp.data.ContactDatasource
import com.piomendes.contactsapp.data.ContactTypeEnum
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.random.Random

class ContactFormViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val contactId: Int = savedStateHandle.get<String>("id")?.toIntOrNull() ?: 0

    var uiState: ContactFormState by mutableStateOf(
        ContactFormState(contactId = contactId)
    )


    init {
        if (!uiState.isNewContact) {
            loadContact()
        }
    }

    fun loadContact() {
        uiState = uiState.copy(isLoading = true, hasErrorLoading = false)

        viewModelScope.launch {
            delay(2000)
            val contact = ContactDatasource.instance.findById(contactId)
            val hasError = Random.nextBoolean()
            uiState = if (contact == null || hasError) {
                uiState.copy(isLoading = false, hasErrorLoading = true)
            } else {
                uiState.copy(
                    isLoading = false,
                    contact = contact,
                    formState = FormState(
                        firstName = FormField(contact.firstName),
                        lastName = FormField(contact.lastName),
                        phoneNumber = FormField(contact.phoneNumber),
                        email = FormField(contact.email),
                        birthDate = FormField(contact.birthDate),
                        assetValue = FormField(contact.assetValue.toString()),
                        isFavorite = FormField(contact.isFavorite),
                        type = FormField(contact.type)
                    )
                )
            }
        }
    }

    fun onFormEvent(event: FormEvent) {
        when(event) {
            is FormEvent.UpdateType -> onTypeChanged(event.newValue)
            is FormEvent.UpdateEmail -> onEmailChanged(event.newValue)
            is FormEvent.UpdateLastName -> onLastNameChange(event.newValue)
            is FormEvent.UpdateFirstName -> onFirstNameChange(event.newValue)
            is FormEvent.UpdateBirthDate -> onBirthDateChanged(event.newValue)
            is FormEvent.UpdateAssetValue -> onAssetValueChanged(event.newValue)
            is FormEvent.UpdateIsFavorite -> onIsFavoriteChanged(event.newValue)
            is FormEvent.UpdatePhoneNumber -> onPhoneNumberChange(event.newValue)
        }
    }

    fun save() {
        if (uiState.isSaving || !isValidForm()) return

        uiState = uiState.copy(isSaving = true, hasErrorSaving = false)

        viewModelScope.launch {
            delay(2000)
            val hasError = Random.nextBoolean()
            uiState = if (hasError) {
                uiState.copy(isSaving = false, hasErrorSaving = true)
            } else {
                val contactToSave = uiState.contact.copy(
                    firstName = uiState.formState.firstName.value,
                    lastName = uiState.formState.lastName.value,
                    phoneNumber = uiState.formState.phoneNumber.value,
                    email = uiState.formState.email.value,
                    birthDate = uiState.formState.birthDate.value,
                    isFavorite = uiState.formState.isFavorite.value,
                    type = uiState.formState.type.value,
                    assetValue = uiState.formState.assetValue.value.let {
                        if (it.isBlank()) {
                            BigDecimal.ZERO
                        } else {
                            BigDecimal(uiState.formState.assetValue.value)
                        }
                    }
                )
                ContactDatasource.instance.save(contactToSave)
                uiState.copy(
                    isSaving = false,
                    contactSaved = true
                )
            }
        }

    }


    private fun onFirstNameChange(newValue: String) {
        if (uiState.formState.firstName.value == newValue) return

        uiState = uiState.copy(
            formState = uiState.formState.copy(
                firstName = FormField(
                    value = newValue,
                    errorMessage = validateFirstName(newValue)
                )
            )
        )
    }

    private fun validateFirstName(value: String): String {
        if (value.isBlank()) return "This field is required"
        return ""
    }

    private fun onLastNameChange(newValue: String) {
        if (uiState.formState.lastName.value == newValue) return
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                lastName = FormField(
                    value = newValue,
                    //errorMessage = THIS FIELD IS NOT REQUIRED
                )
            )
        )
    }

    private fun onPhoneNumberChange(newValue: String) {
        if (uiState.formState.phoneNumber.value == newValue) return
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                phoneNumber = FormField(
                    value = newValue,
                    errorMessage = validatePhoneNumber(newValue)
                )
            )
        )
    }

    private fun validatePhoneNumber(value: String): String {
        if (value.isBlank() ||
            (value.length in 10..11
            && !value.contains(Regex("\\D"))))
                return ""
        return "Invalid phone number (only numbers)"
    }


    private fun onEmailChanged(newValue: String) {
        if (uiState.formState.email.value == newValue) return
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                email = FormField(
                    value = newValue,
                    errorMessage = validateEmail(newValue)
                )
            )
        )
    }

    private fun validateEmail(value: String): String {
        if (value.isNotBlank()
            && !Regex(pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matches(value)
            ) return "Invalid email"
        return ""
    }

    private fun onIsFavoriteChanged(newValue: Boolean) {
        if (uiState.formState.isFavorite.value == newValue) return
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                isFavorite = FormField(value = newValue)
            )
        )
    }

    private fun onAssetValueChanged(newValue: String) {
        if (uiState.formState.assetValue.value == newValue) return
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                assetValue = FormField(
                    value = newValue,
                    errorMessage = validateAssetValue(newValue)
                )
            )
        )
    }

    private fun validateAssetValue(value: String): String {
        if (value.isBlank()) return ""
        try {
            BigDecimal(value)
            return ""
        } catch (_: NumberFormatException) {
            return "Invalid asset value"
        }
    }

    private fun onBirthDateChanged(newValue: LocalDate) {
        if (uiState.formState.birthDate.value == newValue) return
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                birthDate = FormField(value = newValue)
            )
        )
    }

    private fun onTypeChanged(newValue: ContactTypeEnum) {
        if (uiState.formState.type.value == newValue) return
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                type = FormField(value = newValue)
            )
        )
    }

    private fun isValidForm(): Boolean {
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                firstName = uiState.formState.firstName.copy(
                    errorMessage = validateFirstName(uiState.formState.firstName.value)
                ),
                phoneNumber = uiState.formState.phoneNumber.copy(
                    errorMessage = validatePhoneNumber(uiState.formState.phoneNumber.value)
                ),
                email = uiState.formState.email.copy(
                    errorMessage = validateEmail(uiState.formState.email.value)
                ),
                assetValue = uiState.formState.assetValue.copy(
                    errorMessage = validateAssetValue(uiState.formState.assetValue.value)
                )
            )
        )
        return uiState.formState.isValid
    }


} //end of ContactFormViewModel