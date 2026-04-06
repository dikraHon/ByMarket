package com.app.bymarket.domain.models.purchaseModels

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseItem(
    val productName: String = "",
    val quantity: Double = 0.0,
    val price: Double = 0.0,
    val totalPrice: Double = 0.0,
    val barcode: String = ""
)
