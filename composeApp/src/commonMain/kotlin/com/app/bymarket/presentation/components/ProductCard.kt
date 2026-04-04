package com.app.bymarket.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.bymarket.domain.models.Product

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (product.barcodes.isNotEmpty()) {
                Text(
                    text = "Штрихкод: ${product.barcodes.first()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.Bottom
            ) {
                Column {
                    // Цена и единица измерения (квант)
                    Row(verticalAlignment = androidx.compose.ui.Alignment.Bottom) {
                        Text(
                            text = "${product.price} ₽",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = " / ${product.quant} ${product.unitName}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                        )
                    }
                    
                    if (product.bonus > 0) {
                        Text(
                            text = "Скидка: ${product.bonus} ₽",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = (if (product.stock > 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)),
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            text = " В наличии: ${product.stock} ${if (product.type == 1) "кг" else "шт"} ",
                            style = MaterialTheme.typography.labelSmall,
                            color = (if (product.stock > 0) Color(0xFF2E7D32) else Color.Red),
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
                
                Button(
                    onClick = { onAddToCart(product) },
                    enabled = product.stock > 0,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(if (product.stock > 0) "В корзину" else "Нет в наличии", fontSize = 12.sp)
                }
            }
        }
    }
}
