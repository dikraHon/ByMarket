package com.app.bymarket.presentation.screens.mainScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.bymarket.domain.models.productModels.Product
import com.app.bymarket.domain.validation.ProductValidator
import com.app.bymarket.domain.validation.QuantityValidationResult
import com.app.bymarket.presentation.components.QuantityField

@Composable
fun AddToCartDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var quantityText by remember { mutableStateOf(if (product.isWeight) "1.0" else "1") }
    val validationResult = remember(quantityText, product.quant) {
        ProductValidator.validateQuantity(quantityText, product.quant)
    }

    val isError = validationResult !is QuantityValidationResult.Valid && quantityText.isNotEmpty()
    val isInsufficient = validationResult is QuantityValidationResult.InsufficientStock

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
                    color = if (isInsufficient) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                QuantityField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    isWeight = product.isWeight,
                    isError = isError,
                    unitName = product.unitName
                )

                if (isError) {
                    val errorMessage = when (validationResult) {
                        is QuantityValidationResult.InsufficientStock -> "Недостаточно на складе"
                        is QuantityValidationResult.NegativeOrZero -> "Число должно быть больше 0"
                        is QuantityValidationResult.InvalidFormat -> "Некорректный формат"
                        else -> null
                    }
                    
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    quantityText.toDoubleOrNull()?.let { onConfirm(it) }
                },
                enabled = validationResult is QuantityValidationResult.Valid
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
