package com.app.bymarket.presentation.vm.userVm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bymarket.domain.repository.AuthRepository
import com.app.bymarket.domain.repository.PurchaseRepository
import com.app.bymarket.domain.usecase.SignOutUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModel(
    private val authRepository: AuthRepository,
    private val signOutUseCase: SignOutUseCase,
    private val purchaseRepository: PurchaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<UserEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeUserAndHistory()
    }

    private fun observeUserAndHistory() {
        viewModelScope.launch {
            authRepository.authState
                .flatMapLatest { user ->
                    if (user != null) {
                        purchaseRepository.getPurchaseHistory(user.uid)
                            .catch { e ->
                                _effect.emit(UserEffect.Error("Ошибка загрузки истории: ${e.message}"))
                                emit(emptyList())
                            }
                            .map { history -> user to history }
                    } else {
                        flowOf(null to emptyList())
                    }
                }
                .collectLatest { (user, history) ->
                    _state.update { it.copy(
                        user = user,
                        purchaseHistory = history,
                        isLoading = false
                    ) }
                }
        }
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.Logout -> logout()
            is UserEvent.RefreshHistory -> {
                _state.update { it.copy(isLoading = true) }
                observeUserAndHistory()
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            try {
                signOutUseCase()
                _effect.emit(UserEffect.LogoutSuccess)
            } catch (e: Exception) {
                _effect.emit(UserEffect.Error("Ошибка при выходе: ${e.message}"))
            }
        }
    }
}
