package com.app.bymarket.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bymarket.domain.models.userModels.User
import com.app.bymarket.domain.repository.AuthRepository
import com.app.bymarket.domain.usecase.SignOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import com.app.bymarket.domain.models.Purchase
import com.app.bymarket.domain.repository.PurchaseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class UserViewModel(
    private val authRepository: AuthRepository,
    private val signOutUseCase: SignOutUseCase,
    private val purchaseRepository: PurchaseRepository
) : ViewModel() {
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val purchaseHistory: StateFlow<List<Purchase>> = _user.flatMapLatest { currentUser ->
        currentUser?.let {
            purchaseRepository.getPurchaseHistory(it.uid)
        } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.authState.collectLatest { currentUser ->
                _user.value = currentUser
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            signOutUseCase()
            _user.value = null
        }
    }
}
