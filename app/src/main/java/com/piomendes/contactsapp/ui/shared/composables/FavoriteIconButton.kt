package com.piomendes.contactsapp.ui.shared.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.piomendes.contactsapp.R
import com.piomendes.contactsapp.ui.theme.ContactsAppTheme

@Composable
fun FavoriteIconButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onPressed: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onPressed
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

@Preview(showBackground = true)
@Composable
private fun FavoriteIconButtonPreview(modifier: Modifier = Modifier) {
    ContactsAppTheme() {
        FavoriteIconButton(
            isFavorite = false,
            onPressed = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteIconButtonIsFavoritePreview(modifier: Modifier = Modifier) {
    ContactsAppTheme() {
        FavoriteIconButton(
            isFavorite = true,
            onPressed = {}
        )
    }
}