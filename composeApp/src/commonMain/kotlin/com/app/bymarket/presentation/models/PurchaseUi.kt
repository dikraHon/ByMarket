package com.app.bymarket.presentation.models

import com.app.bymarket.domain.models.purchaseModels.Purchase
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

val Purchase.formattedDate: String
    get() {
        val instant = Instant.fromEpochMilliseconds(this.timestamp)
        val period = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val day = period.dayOfMonth.toString().padStart(2, '0')
        val month = period.month.number.toString().padStart(2, '0')
        val year = period.year
        val hour = period.hour.toString().padStart(2, '0')
        val minute = period.minute.toString().padStart(2, '0')
        return "$day.$month.$year $hour:$minute"
    }

val Purchase.formattedAmount: String
    get() = "${this.totalAmount} ₽"

val com.app.bymarket.domain.models.purchaseModels.PurchaseItem.formattedPrice: String
    get() = "$price ₽"

val com.app.bymarket.domain.models.purchaseModels.PurchaseItem.formattedTotalPrice: String
    get() = "$totalPrice ₽"

val com.app.bymarket.domain.models.purchaseModels.PurchaseItem.quantityWithPrice: String
    get() = "$quantity x $formattedPrice"
