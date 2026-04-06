package com.app.bymarket.presentation.screens.authScreens.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NameField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    BaseAuthField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = Icons.Default.Person,
        isError = error != null,
        errorMessage = error,
        modifier = modifier
    )
}
