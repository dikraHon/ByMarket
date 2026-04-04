package com.app.bymarket.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bymarket.data.local.dao.BarcodeDao
import com.app.bymarket.data.local.dao.PackDao
import com.app.bymarket.data.local.dao.PackPriceDao
import com.app.bymarket.data.local.dao.UnitDao
import com.app.bymarket.data.remote.dto.InitialDataDto
import com.app.bymarket.data.remote.dto.toEntity
import com.app.bymarket.domain.models.Product
import com.app.bymarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import bymarket.composeapp.generated.resources.Res

class ProductViewModel(
    private val unitDao: UnitDao,
    private val packDao: PackDao,
    private val barcodeDao: BarcodeDao,
    private val packPriceDao: PackPriceDao,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        seedDataIfNeeded()
        loadProducts()
    }

    private fun seedDataIfNeeded() {
        viewModelScope.launch {
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
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts().collect {
                _products.value = it
            }
        }
    }

    suspend fun searchByBarcode(barcode: String): Product? {
        return productRepository.getProductByBarcode(barcode)
    }
}
