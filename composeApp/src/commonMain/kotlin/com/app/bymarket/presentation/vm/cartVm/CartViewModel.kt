package com.app.bymarket.presentation.vm.cartVm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bymarket.domain.models.productModels.CartItem
import com.app.bymarket.domain.models.purchaseModels.Purchase
import com.app.bymarket.domain.models.purchaseModels.PurchaseItem
import com.app.bymarket.domain.repository.ProductRepository
import com.app.bymarket.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val purchaseRepository: PurchaseRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CartEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: CartEvent) {
        when (event) {
            is CartEvent.AddToCart -> addToCart(event.productId, event.quantity)
            is CartEvent.RemoveFromCart -> removeFromCart(event.productId)
            is CartEvent.UpdateQuantity -> updateQuantity(event.productId, event.quantity)
            is CartEvent.ClearCart -> clearCart()
            is CartEvent.Checkout -> checkout(event.userId)
            is CartEvent.CheckoutAttempt -> handleCheckoutAttempt(event.userId)
        }
    }

    private fun addToCart(productId: Int, quantity: Double) {
        viewModelScope.launch {
            val product = productRepository.getProductById(productId)
            if (product != null) {
                _state.update { currentState ->
                    val items = currentState.items
                    val existingItem = items.find { it.product.id == productId }
                    val newItems = if (existingItem != null) {
                        items.map { 
                            if (it.product.id == productId) it.copy(quantity = it.quantity + quantity) else it 
                        }
                    } else {
                        items + CartItem(product, quantity)
                    }
                    currentState.copy(items = newItems)
                }
            } else {
                _effect.emit(CartEffect.Error("Товар не найден"))
            }
        }
    }

    private fun removeFromCart(productId: Int) {
        _state.update { it.copy(items = it.items.filter { item -> item.product.id != productId }) }
    }

    private fun updateQuantity(productId: Int, quantity: Double) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        _state.update { currentState ->
            val newItems = currentState.items.map {
                if (it.product.id == productId) it.copy(quantity = quantity) else it
            }
            currentState.copy(items = newItems)
        }
    }

    private fun clearCart() {
        _state.update { it.copy(items = emptyList()) }
    }

    private fun handleCheckoutAttempt(userId: String?) {
        if (userId == null) {
            viewModelScope.launch {
                _effect.emit(CartEffect.Error("Необходимо авторизоваться для оплаты"))
            }
            return
        }
        checkout(userId)
    }

    private fun checkout(userId: String) {
        val items = _state.value.items
        if (items.isEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(isProcessing = true) }
            
            val purchase = Purchase(
                timestamp = com.app.bymarket.presentation.vm.currentTimeMillis(),
                totalAmount = _state.value.totalAmount,
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
                    items.forEach { item ->
                        productRepository.reduceProductQuantity(item.product.id, item.quantity)
                    }
                    clearCart()
                    _state.update { it.copy(isProcessing = false) }
                    _effect.emit(CartEffect.CheckoutSuccess)
                }
                .onFailure { error ->
                    _state.update { it.copy(isProcessing = false) }
                    _effect.emit(CartEffect.Error(error.message ?: "Ошибка оформления"))
                }
        }
    }
}
