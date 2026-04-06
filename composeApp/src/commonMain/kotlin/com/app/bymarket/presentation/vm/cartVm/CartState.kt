package com.app.bymarket.presentation.vm.cartVm

import com.app.bymarket.domain.models.productModels.CartItem

data class CartState(
    val items: List<CartItem> = emptyList(),
    val isProcessing: Boolean = false,
    val error: String? = null
) {
    val totalAmount: Double = items.sumOf { it.totalPrice }
    val totalQuantity: Int = items.sumOf { it.quantity }.toInt()
}
