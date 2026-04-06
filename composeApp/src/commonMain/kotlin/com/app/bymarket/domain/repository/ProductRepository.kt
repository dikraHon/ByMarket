package com.app.bymarket.domain.repository

import com.app.bymarket.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductByBarcode(barcode: String): Product?
    suspend fun seedInitialData()
    suspend fun reduceProductQuantity(productId: Int, amount: Double)
}
