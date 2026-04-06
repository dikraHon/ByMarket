package com.app.bymarket.presentation.vm.authVm

data class AuthState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val patronymic: String = "",
    val isLoading: Boolean = false,
    val generalError: String? = null,
    val isSuccess: Boolean = false
)