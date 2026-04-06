package com.app.bymarket.presentation.vm.authVm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.bymarket.domain.models.userModels.LoginData
import com.app.bymarket.domain.models.userModels.RegistrationData
import com.app.bymarket.domain.usecase.SignInUseCase
import com.app.bymarket.domain.usecase.SignUpUseCase
import com.app.bymarket.domain.validation.AuthValidationError
import com.app.bymarket.domain.validation.AuthValidator
import com.app.bymarket.domain.validation.ValidationResult
import com.app.bymarket.util.CrashLogger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _isSuccess = MutableSharedFlow<Boolean>()
    val isSuccess: SharedFlow<Boolean> = _isSuccess

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> {
                val error = when (val res = AuthValidator.validateEmail(event.value)) {
                    is ValidationResult.Error -> res.message
                    else -> null
                }
                _state.update { it.copy(email = event.value.trim(), emailError = error) }
            }
            is AuthEvent.PasswordChanged -> {
                val error = when (val res = AuthValidator.validatePassword(event.value)) {
                    is ValidationResult.Error -> res.message
                    else -> null
                }
                _state.update { it.copy(password = event.value.trim(), passwordError = error) }
            }
            is AuthEvent.FirstNameChanged -> {
                val error = when (val res = AuthValidator.validateName(event.value, "имя")) {
                    is ValidationResult.Error -> res.message
                    else -> null
                }
                _state.update { it.copy(firstName = event.value, firstNameError = error) }
            }
            is AuthEvent.LastNameChanged -> {
                val error = when (val res = AuthValidator.validateName(event.value, "фамилию")) {
                    is ValidationResult.Error -> res.message
                    else -> null
                }
                _state.update { it.copy(lastName = event.value, lastNameError = error) }
            }
            is AuthEvent.PatronymicChanged -> {
                _state.update { it.copy(patronymic = event.value) }
            }
            is AuthEvent.LoginClicked -> login()
            is AuthEvent.SignUpClicked -> signUp()
            is AuthEvent.ErrorDismissed -> _state.update { it.copy(generalError = null) }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalError = null) }

            val result = signInUseCase(LoginData(_state.value.email, _state.value.password))

            result.onSuccess {
                _isSuccess.emit(true)
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }
            result.onFailure { e ->
                handleAuthError(e)
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, generalError = null) }

            val registrationData = RegistrationData(
                email = _state.value.email,
                password = _state.value.password,
                firstName = _state.value.firstName,
                lastName = _state.value.lastName,
                patronymic = _state.value.patronymic.ifEmpty { null }
            )

            val result = signUpUseCase(registrationData)

            result.onSuccess {
                _isSuccess.emit(true)
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            }
            result.onFailure { e ->
                handleAuthError(e)
            }
        }
    }

    private fun handleAuthError(e: Throwable) {
        if (e is AuthValidationError) {
            _state.update { it.copy(
                isLoading = false,
                emailError = e.emailError,
                passwordError = e.passwordError,
                firstNameError = e.firstNameError,
                lastNameError = e.lastNameError
            ) }
        } else {
            _state.update { it.copy(isLoading = false, generalError = e.message) }
            CrashLogger.recordException(e)
        }
    }
}