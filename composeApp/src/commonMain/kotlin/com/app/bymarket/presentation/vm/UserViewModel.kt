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

class UserViewModel(
    private val authRepository: AuthRepository,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

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
