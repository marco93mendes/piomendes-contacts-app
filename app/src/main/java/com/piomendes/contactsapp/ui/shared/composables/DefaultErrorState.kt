package com.piomendes.contactsapp.ui.shared.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.piomendes.contactsapp.R
import com.piomendes.contactsapp.ui.theme.ContactsAppTheme


@Composable
fun DefaultErrorState(
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

@Preview(showBackground = true, heightDp = 500)
@Composable
private fun DefaultErrorStatePreview() {
    ContactsAppTheme {
        DefaultErrorState(
            onReloadPressed = {}
        )
    }
}