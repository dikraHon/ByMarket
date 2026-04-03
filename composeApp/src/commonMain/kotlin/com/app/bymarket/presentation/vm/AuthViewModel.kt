package com.app.bymarket.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bymarket.domain.models.userModels.LoginData
import com.app.bymarket.domain.models.userModels.RegistrationData
import com.app.bymarket.domain.usecase.SignInUseCase
import com.app.bymarket.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName

    private val _patronymic = MutableStateFlow("")
    val patronymic: StateFlow<String> = _patronymic

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    private val _isSuccess = MutableSharedFlow<Boolean>()
    val isSuccess: SharedFlow<Boolean> = _isSuccess

    fun onEmailChanged(value: String) { _email.value = value }
    fun onPasswordChanged(value: String) { _password.value = value }
    fun onFirstNameChanged(value: String) { _firstName.value = value }
    fun onLastNameChanged(value: String) { _lastName.value = value }
    fun onPatronymicChanged(value: String) { _patronymic.value = value }

    fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = signInUseCase(LoginData(_email.value, _password.value))
            result.onSuccess {
                _isSuccess.emit(true)
            }
            result.onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val registrationData = RegistrationData(
                email = _email.value,
                password = _password.value,
                firstName = _firstName.value,
                lastName = _lastName.value,
                patronymic = _patronymic.value.ifEmpty { null }
            )
            val result = signUpUseCase(registrationData)
            result.onSuccess {
                _isSuccess.emit(true)
            }
            result.onFailure { _error.value = it.message }
            _isLoading.value = false
        }
    }
}
