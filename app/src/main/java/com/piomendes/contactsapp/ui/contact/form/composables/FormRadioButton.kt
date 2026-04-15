package com.piomendes.contactsapp.ui.contact.form.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.piomendes.contactsapp.data.ContactTypeEnum
import com.piomendes.contactsapp.ui.theme.ContactsAppTheme

@Composable
fun FormRadioButton(
    modifier: Modifier = Modifier,
    label: String,
    value: ContactTypeEnum,
    groupValue: ContactTypeEnum,
    onValueChanged: (ContactTypeEnum) -> Unit,
    enabled: Boolean = true
) {
    Row(
      modifier = modifier
          .clickable { onValueChanged(value) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = value == groupValue,
            onClick = { onValueChanged(value) },
            enabled = enabled
        )
        Text(label)
    }
}

@Preview(showBackground = true)
@Composable
private fun FormRadioButtonPreview() {
    ContactsAppTheme {
        var groupValue by remember { mutableStateOf(ContactTypeEnum.PROFESSIONAL) }
        Column {
            FormRadioButton(
                modifier = Modifier.padding(20.dp),
                label = "Personal",
                value = ContactTypeEnum.PERSONAL,
                groupValue = groupValue,
                onValueChanged = { newValue ->
                    groupValue = newValue
                }
            )
            FormRadioButton(
                modifier = Modifier.padding(20.dp),
                label = "Professional",
                value = ContactTypeEnum.PROFESSIONAL,
                groupValue = groupValue,
                onValueChanged = { newValue ->
                    groupValue = newValue
                }
            )
        }
    }
}