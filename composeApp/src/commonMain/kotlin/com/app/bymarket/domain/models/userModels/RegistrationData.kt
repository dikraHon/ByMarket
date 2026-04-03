package com.app.bymarket.domain.models.userModels

data class RegistrationData(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val patronymic: String? = null
)
