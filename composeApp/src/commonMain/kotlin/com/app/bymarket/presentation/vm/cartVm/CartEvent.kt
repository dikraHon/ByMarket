package com.app.bymarket.presentation.vm.cartVm

sealed class CartEvent {
    data class AddToCart(val productId: Int, val quantity: Double) : CartEvent()
    data class UpdateQuantity(val productId: Int, val quantity: Double) : CartEvent()
    data class RemoveFromCart(val productId: Int) : CartEvent()
    data class Checkout(val userId: String) : CartEvent()
    data class CheckoutAttempt(val userId: String?) : CartEvent()
    object ClearCart : CartEvent()
}
