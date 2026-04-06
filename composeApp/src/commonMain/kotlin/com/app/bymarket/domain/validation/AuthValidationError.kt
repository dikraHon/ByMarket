package com.app.bymarket.domain.validation

data class AuthValidationError(
    val emailError: String? = null,
    val passwordError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null
) : Exception("Validation failed")