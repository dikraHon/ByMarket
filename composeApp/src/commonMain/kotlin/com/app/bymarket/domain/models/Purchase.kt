package com.app.bymarket.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Purchase(
    val id: String = "",
    val timestamp: Long = 0,
    val totalAmount: Double = 0.0,
    val items: List<PurchaseItem> = emptyList()
)

@Serializable
data class PurchaseItem(
    val productName: String = "",
    val quantity: Double = 0.0,
    val price: Double = 0.0,
    val totalPrice: Double = 0.0,
    val barcode: String = ""
)
