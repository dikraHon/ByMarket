package com.app.bymarket.presentation.vm

import androidx.lifecycle.ViewModel
import com.app.bymarket.domain.models.productModels.CartItem
import com.app.bymarket.domain.models.productModels.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.app.bymarket.domain.models.purchaseModels.Purchase
import com.app.bymarket.domain.models.purchaseModels.PurchaseItem
import com.app.bymarket.domain.repository.PurchaseRepository
import com.app.bymarket.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class CartViewModel(
    private val purchaseRepository: PurchaseRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _checkoutSuccess = MutableSharedFlow<Unit>()
    val checkoutSuccess: SharedFlow<Unit> = _checkoutSuccess.asSharedFlow()

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()

    fun addToCart(product: Product, quantity: Double = 1.0) {
        _cartItems.update { items ->
            val existingItem = items.find { it.product.id == product.id }
            if (existingItem != null) {
                items.map { 
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity) else it 
                }
            } else {
                items + CartItem(product, quantity)
            }
        }
    }

    fun removeFromCart(productId: Int) {
        _cartItems.update { items ->
            items.filter { it.product.id != productId }
        }
    }

    fun updateQuantity(productId: Int, quantity: Double) {
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

    fun checkout(userId: String) {
        val items = _cartItems.value
        if (items.isEmpty()) return

        viewModelScope.launch {
            val purchase = Purchase(
                timestamp = currentTimeMillis(),
                totalAmount = totalAmount,
                items = items.map {
                    PurchaseItem(
                        productName = it.product.name,
                        quantity = it.quantity,
                        price = it.product.finalPrice,
                        totalPrice = it.totalPrice,
                        barcode = it.product.id.toString()
                    )
                }
            )

            purchaseRepository.savePurchase(userId, purchase)
                .onSuccess {
                    // СПИСАНИЕ СО СКЛАДА
                    items.forEach { item ->
                        productRepository.reduceProductQuantity(item.product.id, item.quantity)
                    }

                    clearCart()
                    _checkoutSuccess.emit(Unit)
                }
                .onFailure {
                    _error.emit(it.message ?: "Ошибка при оформлении заказа")
                }
        }
    }

    val totalAmount: Double
        get() = _cartItems.value.sumOf { it.totalPrice }
}
