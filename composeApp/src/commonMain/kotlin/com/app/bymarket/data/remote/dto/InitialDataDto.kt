package com.app.bymarket.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class InitialDataDto(
    val units: List<UnitDto>,
    val packs: List<PackDto>,
    val barcodes: List<BarcodeDto>,
    val prices: List<PriceDto>
)
