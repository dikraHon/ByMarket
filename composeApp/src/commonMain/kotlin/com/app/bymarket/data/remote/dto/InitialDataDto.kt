package com.app.bymarket.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class InitialDataDto(
    val units: List<UnitDto>,
    val packs: List<PackDto>,
    val barcodes: List<BarcodeDto>,
    val prices: List<PriceDto>
)

@Serializable
data class UnitDto(
    val id: Int,
    val name: String
)

@Serializable
data class PackDto(
    val id: Int,
    val unitId: Int,
    val name: String,
    val type: Int,
    val quant: Double
)

@Serializable
data class BarcodeDto(
    val id: Int,
    val packId: Int,
    val body: String
)

@Serializable
data class PriceDto(
    val id: Int,
    val packId: Int,
    val price: Int,
    val bonus: Int
)
