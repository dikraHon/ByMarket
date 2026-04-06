package com.app.bymarket.presentation.vm.cartVm

sealed class CartEffect {
    object CheckoutSuccess : CartEffect()
    data class Error(val message: String) : CartEffect()
}
