package com.app.bymarket.domain.repository

import com.app.bymarket.domain.models.productModels.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductByBarcode(barcode: String): Product?
    suspend fun getProductById(id: Int): Product?
    suspend fun seedInitialData()
    suspend fun reduceProductQuantity(productId: Int, amount: Double)
}
