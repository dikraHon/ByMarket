package com.app.bymarket.domain.models.purchaseModels

import kotlinx.serialization.Serializable

@Serializable
data class Purchase(
    val id: String = "",
    val timestamp: Long = 0,
    val totalAmount: Double = 0.0,
    val items: List<PurchaseItem> = emptyList()
)