package com.app.bymarket.presentation.vm.productVm

import com.app.bymarket.domain.models.productModels.Product

data class ProductState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val barcodeInput: String = "",
    val isScanning: Boolean = false,
    val selectedProductForAdd: Product? = null,
    val error: String? = null
)
