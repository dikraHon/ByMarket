package com.app.bymarket.data.repository

import com.app.bymarket.domain.models.Purchase
import com.app.bymarket.domain.repository.PurchaseRepository
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PurchaseRepositoryImpl(
    private val firestore: FirebaseFirestore
) : PurchaseRepository {
    override suspend fun savePurchase(userId: String, purchase: Purchase): Result<Unit> = runCatching {
        firestore.collection("users")
            .document(userId)
            .collection("purchases")
            .add(purchase)
    }

    override fun getPurchaseHistory(userId: String): Flow<List<Purchase>> {
        return firestore.collection("users")
            .document(userId)
            .collection("purchases")
            .snapshots
            .map { querySnapshot ->
                querySnapshot.documents.map { it.data<Purchase>().copy(id = it.id) }
                    .sortedByDescending { it.timestamp }
            }
    }
}
