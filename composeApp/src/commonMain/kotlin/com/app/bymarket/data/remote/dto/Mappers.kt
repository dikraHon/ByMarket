package com.app.bymarket.data.remote.dto

import com.app.bymarket.data.local.entities.BarcodeEntity
import com.app.bymarket.data.local.entities.PackEntity
import com.app.bymarket.data.local.entities.PackPriceEntity
import com.app.bymarket.data.local.entities.UnitEntity

fun UnitDto.toEntity() = UnitEntity(id = id, name = name)
fun PackDto.toEntity() = PackEntity(id = id, unitId = unitId, name = name, type = type, quant = quant)
fun BarcodeDto.toEntity() = BarcodeEntity(id = id, packId = packId, body = body)
fun PriceDto.toEntity() = PackPriceEntity(id = id, packId = packId, price = price, bonus = bonus)
