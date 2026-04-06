package com.app.bymarket.presentation.screens.authScreens.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    BaseAuthField(
        value = value,
        onValueChange = onValueChange,
        label = "Email",
        placeholder = "example@mail.com",
        leadingIcon = Icons.Default.Email,
        keyboardType = KeyboardType.Email,
        isError = error != null,
        errorMessage = error,
        modifier = modifier,
        imeAction = imeAction,
        keyboardActions = keyboardActions
    )
}