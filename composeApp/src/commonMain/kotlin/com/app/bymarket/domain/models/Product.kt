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
    val displayPrice: String get() = "${price.toString()} ₽"
    val displayBonus: String get() = if (bonus > 0) "-${bonus.toString()} ₽" else ""
}
