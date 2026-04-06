package com.app.bymarket.domain.models

data class Product(
    val id: Int,
    val name: String,
    val unitName: String,
    val price: Double,
    val bonus: Double,
    val barcodes: List<String>,
    val type: Int,
    val quant: Int,
    val stock: Double
) {
    val finalPrice: Double get() = (price - bonus).coerceAtLeast(0.0)
    val isWeight: Boolean get() = type == 1
    
    val displayPrice: String get() = "${price} ₽"
    val displayFinalPrice: String get() = "${finalPrice} ₽"
}
