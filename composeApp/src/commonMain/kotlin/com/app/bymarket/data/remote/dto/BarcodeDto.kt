package com.app.bymarket.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BarcodeDto(
    val id: Int,
    val packId: Int,
    val body: String
)