package com.app.bymarket.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class PriceDto(
    val id: Int,
    val packId: Int,
    val price: Int,
    val bonus: Int
)
