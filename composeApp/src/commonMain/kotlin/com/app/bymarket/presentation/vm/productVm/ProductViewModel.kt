package com.app.bymarket.presentation.vm.productVm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bymarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductState())
    val state = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadProducts()
    }

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.BarcodeInputChanged -> {
                _state.update { it.copy(barcodeInput = event.value) }
            }
            is ProductEvent.SearchClicked -> {
                searchProduct(_state.value.barcodeInput)
            }
            is ProductEvent.ScanResult -> {
                _state.update { it.copy(barcodeInput = event.barcode, isScanning = false) }
                searchProduct(event.barcode)
            }
            is ProductEvent.StartScanning -> {
                _state.update { it.copy(isScanning = true) }
            }
            is ProductEvent.StopScanning -> {
                _state.update { it.copy(isScanning = false) }
            }
            is ProductEvent.ProductSelected -> {
                _state.update { it.copy(selectedProductForAdd = event.product) }
            }
            is ProductEvent.ErrorDismissed -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            productRepository.seedInitialData()
            productRepository.getAllProducts().collect { products ->
                _state.update { it.copy(products = products, isLoading = false) }
            }
        }
    }

    private fun searchProduct(barcode: String) {
        if (barcode.isBlank()) return
        
        viewModelScope.launch {
            val product = productRepository.getProductByBarcode(barcode)
            if (product != null) {
                _state.update { it.copy(selectedProductForAdd = product, barcodeInput = "") }
            } else {
                _uiEvent.emit("Товар не найден")
            }
        }
    }
}
