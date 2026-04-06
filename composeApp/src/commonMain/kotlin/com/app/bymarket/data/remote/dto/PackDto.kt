package com.app.bymarket.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PackDto(
    val id: Int,
    val unitId: Int,
    val name: String,
    val type: Int,
    val quant: Double
)