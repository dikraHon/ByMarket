package com.app.bymarket.domain.validation

sealed class QuantityValidationResult {
    object Valid : QuantityValidationResult()
    object Empty : QuantityValidationResult()
    object InvalidFormat : QuantityValidationResult()
    object NegativeOrZero : QuantityValidationResult()
    data class InsufficientStock(val available: Double) : QuantityValidationResult()
}

object ProductValidator {
    fun validateQuantity(input: String, stock: Double): QuantityValidationResult {
        if (input.isBlank()) return QuantityValidationResult.Empty
        
        val value = input.toDoubleOrNull() ?: return QuantityValidationResult.InvalidFormat
        
        return when {
            value <= 0 -> QuantityValidationResult.NegativeOrZero
            value > stock -> QuantityValidationResult.InsufficientStock(stock)
            else -> QuantityValidationResult.Valid
        }
    }
}
