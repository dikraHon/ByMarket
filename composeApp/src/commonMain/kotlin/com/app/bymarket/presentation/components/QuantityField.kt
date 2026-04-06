package com.app.bymarket.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun QuantityField(
    value: String,
    onValueChange: (String) -> Unit,
    isWeight: Boolean,
    isError: Boolean,
    unitName: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val filtered = if (isWeight) {
                if (input.all { it.isDigit() || it == '.' } && input.count { it == '.' } <= 1) {
                    input
                } else value
            } else {
                if (input.all { it.isDigit() }) {
                    input
                } else value
            }
            onValueChange(filtered)
        },
        label = { Text("Количество ($unitName)") },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isWeight) KeyboardType.Decimal else KeyboardType.Number
        ),
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        singleLine = true
    )
}
