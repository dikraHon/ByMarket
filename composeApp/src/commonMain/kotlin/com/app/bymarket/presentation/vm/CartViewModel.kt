package com.app.bymarket.presentation.vm

import androidx.lifecycle.ViewModel
import com.app.bymarket.domain.models.CartItem
import com.app.bymarket.domain.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(product: Product) {
        _cartItems.update { items ->
            val existingItem = items.find { it.product.id == product.id }
            if (existingItem != null) {
                items.map { 
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it 
                }
            } else {
                items + CartItem(product, 1)
            }
        }
    }

    fun removeFromCart(productId: Int) {
        _cartItems.update { items ->
            items.filter { it.product.id != productId }
        }
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        _cartItems.update { items ->
            items.map {
                if (it.product.id == productId) it.copy(quantity = quantity) else it
            }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    val totalAmount: Double
        get() = _cartItems.value.sumOf { it.totalPrice }
}
