package com.app.bymarket.presentation.screens.cartScreens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.bymarket.domain.models.productModels.CartItem

@Composable
fun CartItemRow(
    item: CartItem,
    onUpdateQuantity: (Int, Double) -> Unit,
    onRemove: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleSmall
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${item.product.finalPrice} ₽",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    val displayQuantity = if (item.product.isWeight) {
                        "${item.quantity} кг"
                    } else {
                        "${item.quantity.toInt()} шт"
                    }
                    
                    Text(
                        text = " x $displayQuantity",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "Сумма: ${item.totalPrice} ₽",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                val step = if (item.product.isWeight) 0.1 else 1.0
                
                IconButton(onClick = { onUpdateQuantity(item.product.id, item.quantity - step) }) {
                    Icon(Icons.Default.Remove, contentDescription = "Меньше")
                }
                
                Text(
                    text = if (item.product.isWeight) {
                        ((item.quantity * 10).toInt() / 10.0).toString()
                    } else {
                        item.quantity.toInt().toString()
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(onClick = { 
                    onUpdateQuantity(item.product.id, item.quantity + step)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Больше")
                }
                
                IconButton(onClick = { onRemove(item.product.id) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
