package com.app.bymarket.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bymarket.domain.models.userModels.LoginData
import com.app.bymarket.domain.models.userModels.RegistrationData
import com.app.bymarket.domain.usecase.SignInUseCase
import com.app.bymarket.domain.usecase.SignUpUseCase
import com.app.bymarket.domain.validation.AuthValidator
import com.app.bymarket.domain.validation.ValidationResult
import com.app.bymarket.util.CrashLogger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()
    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    private val _firstName = MutableStateFlow("")
    val firstName = _firstName.asStateFlow()
    private val _firstNameError = MutableStateFlow<String?>(null)
    val firstNameError = _firstNameError.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName = _lastName.asStateFlow()
    private val _lastNameError = MutableStateFlow<String?>(null)
    val lastNameError = _lastNameError.asStateFlow()

    private val _patronymic = MutableStateFlow("")
    val patronymic = _patronymic.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    
    private val _isSuccess = MutableSharedFlow<Boolean>()
    val isSuccess: SharedFlow<Boolean> = _isSuccess

    fun onEmailChanged(value: String) { 
        _email.value = value.trim()
        _emailError.value = when (val result = AuthValidator.validateEmail(value)) {
            is ValidationResult.Error -> result.message
            else -> null
        }
    }

    fun onPasswordChanged(value: String) { 
        _password.value = value.trim()
        _passwordError.value = when (val result = AuthValidator.validatePassword(value)) {
            is ValidationResult.Error -> result.message
            else -> null
        }
    }

    fun onFirstNameChanged(value: String) { 
        _firstName.value = value
        _firstNameError.value = when (val result = AuthValidator.validateName(value, "имя")) {
            is ValidationResult.Error -> result.message
            else -> null
        }
    }

    fun onLastNameChanged(value: String) { 
        _lastName.value = value
        _lastNameError.value = when (val result = AuthValidator.validateName(value, "фамилию")) {
            is ValidationResult.Error -> result.message
            else -> null
        }
    }

    fun onPatronymicChanged(value: String) { _patronymic.value = value }

    fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = signInUseCase(LoginData(_email.value, _password.value))
            result.onSuccess {
                _isSuccess.emit(true)
            }
            result.onFailure { 
                _error.value = it.message 
                CrashLogger.recordException(it)
            }
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
            result.onFailure { 
                _error.value = it.message 
                CrashLogger.recordException(it)
            }
            _isLoading.value = false
        }
    }
}
