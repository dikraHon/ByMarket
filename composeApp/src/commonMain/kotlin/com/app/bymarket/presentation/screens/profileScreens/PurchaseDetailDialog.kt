package com.app.bymarket.presentation.screens.profileScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.app.bymarket.domain.models.Purchase

@Composable
fun PurchaseDetailDialog(purchase: Purchase, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Детали покупки", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                    items(purchase.items) { item ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(item.productName, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${item.quantity} x ${item.price} ₽")
                                Text("${item.totalPrice} ₽", fontWeight = FontWeight.SemiBold)
                            }
                            Text(
                                "Штрихкод: ${item.barcode}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            HorizontalDivider(modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("ИТОГО:", fontWeight = FontWeight.Bold)
                    Text("${purchase.totalAmount} ₽", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End).padding(top = 16.dp)
                ) {
                    Text("Закрыть")
                }
            }
        }
    }
}
