package com.app.bymarket.presentation.screens.profileScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.bymarket.domain.models.purchaseModels.Purchase
import com.app.bymarket.presentation.components.BarcodeImage
import com.app.bymarket.presentation.models.formattedAmount
import com.app.bymarket.presentation.models.formattedDate

@Composable
fun PurchaseHistoryItem(
    purchase: Purchase,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = purchase.formattedDate,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = purchase.formattedAmount,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Подробнее",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
            
            purchase.items.firstOrNull()?.barcode?.takeIf { it.isNotBlank() }?.let { barcode ->
                Spacer(modifier = Modifier.height(12.dp))
                BarcodeImage(
                    barcode = barcode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
            }
        }
    }
}
