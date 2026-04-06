package com.app.bymarket.presentation.screens.mainScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app.bymarket.domain.models.productModels.Product

@Composable
fun AddToCartDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var quantityText by remember { mutableStateOf(if (product.isWeight) "1.0" else "1") }
    val quantity = quantityText.toDoubleOrNull() ?: 0.0
    val isValid = quantity > 0 && quantity <= product.quant

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Добавление в корзину") },
        text = {
            Column {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "В наличии: ${product.quant} ${product.unitName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = quantityText,
                    onValueChange = {
                        if (product.isWeight) {
                            if (it.all { char -> char.isDigit() || char == '.' } && it.count { char -> char == '.' } <= 1) {
                                quantityText = it
                            }
                        } else {
                            if (it.all { char -> char.isDigit() }) {
                                quantityText = it
                            }
                        }
                    },
                    label = { Text("Количество (${if (product.isWeight) "кг" else "шт"})") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (product.isWeight) KeyboardType.Decimal else KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValid && quantityText.isNotEmpty()
                )
                if (!isValid && quantityText.isNotEmpty()) {
                    Text(
                        text = if (quantity > product.quant) "Недостаточно на складе" else "Введите корректное число",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(quantity) },
                enabled = isValid
            ) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
