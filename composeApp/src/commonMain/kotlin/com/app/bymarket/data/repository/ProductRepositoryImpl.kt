package com.app.bymarket.data.repository

import com.app.bymarket.data.local.dao.BarcodeDao
import com.app.bymarket.data.local.dao.PackDao
import com.app.bymarket.data.local.dao.PackPriceDao
import com.app.bymarket.data.local.dao.UnitDao
import com.app.bymarket.data.remote.dto.InitialDataDto
import com.app.bymarket.data.remote.dto.toEntity
import com.app.bymarket.domain.models.Product
import com.app.bymarket.domain.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.serialization.json.Json
import bymarket.composeapp.generated.resources.Res

class ProductRepositoryImpl(
    private val unitDao: UnitDao,
    private val packDao: PackDao,
    private val barcodeDao: BarcodeDao,
    private val packPriceDao: PackPriceDao
) : ProductRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllProducts(): Flow<List<Product>> {
        return combine(
            packDao.getAllPacks(),
            unitDao.getAllUnits()
        ) { packs, units ->
            packs.map { pack ->
                val unit = units.find { it.id == pack.unitId }
                Product(
                    id = pack.id,
                    name = pack.name,
                    unitName = unit?.name ?: "",
                    price = 0.0,
                    bonus = 0.0,
                    barcodes = emptyList(),
                    type = pack.type,
                    quant = pack.quant
                )
            }
        }.flatMapLatest { products ->
            if (products.isEmpty()) return@flatMapLatest kotlinx.coroutines.flow.flowOf(emptyList())

            combine(
                products.map { product ->
                    combine(
                        barcodeDao.getBarcodesByPackId(product.id),
                        packPriceDao.getPricesByPackId(product.id)
                    ) { barcodes, prices ->
                        val firstPrice = prices.firstOrNull()
                        product.copy(
                            price = (firstPrice?.price ?: 0) / 100.0,
                            bonus = (firstPrice?.bonus ?: 0) / 100.0,
                            barcodes = barcodes.map { it.body }
                        )
                    }
                }
            ) { it.toList() }
        }
    }

    override suspend fun getProductByBarcode(barcode: String): Product? {
        val pack = packDao.getPackByBarcode(barcode) ?: return null
        val units = unitDao.getAllUnits().first()
        val unit = units.find { it.id == pack.unitId }
        val barcodes = barcodeDao.getBarcodesByPackId(pack.id).first()
        val prices = packPriceDao.getPricesByPackId(pack.id).first()
        val firstPrice = prices.firstOrNull()

        return Product(
            id = pack.id,
            name = pack.name,
            unitName = unit?.name ?: "",
            price = (firstPrice?.price ?: 0) / 100.0,
            bonus = (firstPrice?.bonus ?: 0) / 100.0,
            barcodes = barcodes.map { it.body },
            type = pack.type,
            quant = pack.quant
        )
    }

    override suspend fun seedInitialData() {
        val currentPacks = packDao.getAllPacks().first()
        if (currentPacks.isEmpty()) {
            try {
                val jsonString = Res.readBytes("files/initial_data.json").decodeToString()
                val initialData = Json.decodeFromString<InitialDataDto>(jsonString)
                unitDao.insert(initialData.units.map { it.toEntity() })
                packDao.insert(initialData.packs.map { it.toEntity() })
                barcodeDao.insert(initialData.barcodes.map { it.toEntity() })
                packPriceDao.insert(initialData.prices.map { it.toEntity() })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun reduceProductQuantity(productId: Int, amount: Double) {
        packDao.reduceQuantity(productId, amount)
    }
}
