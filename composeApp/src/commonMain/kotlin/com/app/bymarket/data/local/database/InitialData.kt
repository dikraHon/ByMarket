package com.app.bymarket.data.local.database

import com.app.bymarket.data.local.entities.BarcodeEntity
import com.app.bymarket.data.local.entities.PackEntity
import com.app.bymarket.data.local.entities.PackPriceEntity
import com.app.bymarket.data.local.entities.UnitEntity

object InitialData {
    val units = listOf(
        UnitEntity(1, "кг"),
        UnitEntity(2, "шт"),
        UnitEntity(3, "л")
    )

    val packs = listOf(
        PackEntity(1, 1, "Яблоки", 1, 1000),
        PackEntity(2, 2, "Хлеб", 0, 1),
        PackEntity(3, 3, "Молоко", 0, 1)
    )

    val barcodes = listOf(
        BarcodeEntity(1, 1, "1234567890"),
        BarcodeEntity(2, 2, "0987654321"),
        BarcodeEntity(3, 3, "5555555555")
    )

    val prices = listOf(
        PackPriceEntity(1, 1, 15000, 1000),
        PackPriceEntity(2, 2, 5000, 0),
        PackPriceEntity(3, 3, 8000, 500)
    )
}
