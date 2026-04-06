package com.app.bymarket.presentation.screens.authScreens.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

@Composable
fun NameField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    BaseAuthField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = Icons.Default.Person,
        isError = error != null,
        errorMessage = error,
        modifier = modifier,
        imeAction = imeAction,
        keyboardActions = keyboardActions
    )
}
