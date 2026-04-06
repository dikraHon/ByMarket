package com.app.bymarket.presentation.vm.productVm

import com.app.bymarket.domain.models.productModels.Product

sealed class ProductEvent {
    data class BarcodeInputChanged(val value: String) : ProductEvent()
    object SearchClicked : ProductEvent()
    data class ScanResult(val barcode: String) : ProductEvent()
    object StartScanning : ProductEvent()
    object StopScanning : ProductEvent()
    data class ProductSelected(val product: Product?) : ProductEvent()
    object ErrorDismissed : ProductEvent()
}
