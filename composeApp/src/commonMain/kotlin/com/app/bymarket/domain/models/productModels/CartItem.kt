package com.app.bymarket.domain.models.productModels

data class CartItem(
    val product: Product,
    val quantity: Double
) {
    val totalPrice: Double get() = product.finalPrice * quantity
}