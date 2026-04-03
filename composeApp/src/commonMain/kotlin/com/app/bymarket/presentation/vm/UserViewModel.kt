package com.app.bymarket.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bymarket.domain.models.userModels.User
import com.app.bymarket.domain.usecase.GetCurrentUserUseCase
import com.app.bymarket.domain.usecase.SignOutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchUserData()
    }

    fun fetchUserData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _user.value = getCurrentUserUseCase()
            } catch (_: Exception) {
                _user.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            signOutUseCase()
            _user.value = null
            _isLoading.value = false
        }
    }
}
