package com.app.bymarket.domain.models.userModels

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val patronymic: String? = null
)
