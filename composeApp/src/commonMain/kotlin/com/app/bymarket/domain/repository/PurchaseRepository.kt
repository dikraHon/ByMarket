package com.app.bymarket.domain.repository

import com.app.bymarket.domain.models.purchaseModels.Purchase
import kotlinx.coroutines.flow.Flow

interface PurchaseRepository {
    suspend fun savePurchase(userId: String, purchase: Purchase): Result<Unit>
    fun getPurchaseHistory(userId: String): Flow<List<Purchase>>
}
