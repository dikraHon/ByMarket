package com.app.bymarket.presentation.screens.authScreens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
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
        modifier = modifier
    )
}