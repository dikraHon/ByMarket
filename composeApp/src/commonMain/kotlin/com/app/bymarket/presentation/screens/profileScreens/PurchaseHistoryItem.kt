package com.app.bymarket.presentation.screens.profileScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.bymarket.domain.models.purchaseModels.Purchase
import com.app.bymarket.presentation.components.BarcodeImage
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

@Composable
fun PurchaseHistoryItem(purchase: Purchase, onClick: () -> Unit) {
    val date = remember(purchase.timestamp) {
        val instant = Instant.fromEpochMilliseconds(purchase.timestamp)
        val period = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val day = period.dayOfMonth.toString().padStart(2, '0')
        val month = period.month.number.toString().padStart(2, '0')
        val year = period.year
        val hour = period.hour.toString().padStart(2, '0')
        val minute = period.minute.toString().padStart(2, '0')
        "$day.$month.$year $hour:$minute"
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(date, fontWeight = FontWeight.Medium)
                    Text("${purchase.totalAmount} ₽", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.Info, contentDescription = "Подробнее", tint = MaterialTheme.colorScheme.outline)
            }

            // Выводим штрих-код первого товара для быстрого доступа
            purchase.items.firstOrNull()?.barcode?.let { firstBarcode ->
                if (firstBarcode.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    BarcodeImage(
                        barcode = firstBarcode,
                        modifier = Modifier.height(60.dp) // Чуть компактнее для карточки
                    )
                }
            }
        }
    }
}
