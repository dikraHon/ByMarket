package com.app.bymarket.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UnitDto(
    val id: Int,
    val name: String
)