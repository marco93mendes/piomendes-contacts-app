package com.piomendes.contactsapp.ui.contact.form.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.piomendes.contactsapp.ui.theme.ContactsAppTheme

@Composable
fun FormCheckbox(
    modifier: Modifier = Modifier,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .clickable { onCheckedChange(!checked) },
            verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
           modifier = modifier,
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
        Text(label)
    }
}

@Preview(showBackground = true)
@Composable
fun FormCheckboxPreview() {
    var checked by remember { mutableStateOf(true) }
    ContactsAppTheme {
        FormCheckbox(
            label = "Favorite",
            checked = checked,
            onCheckedChange = { newValue ->
                checked = newValue
            }
        )
    }
}