package com.app.bymarket.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.bymarket.presentation.vm.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onNavigateBack: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Корзина") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Итого:", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "${viewModel.totalAmount} ₽",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Button(onClick = { /* TODO: Checkout */ }) {
                            Text("Оформить заказ")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Корзина пуста", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(cartItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.product.name, fontWeight = FontWeight.Bold)
                                Text("${item.product.price} ₽ / ${item.product.unitName}", fontSize = 12.sp)
                            }
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { viewModel.updateQuantity(item.product.id, item.quantity - 1) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Меньше")
                                }
                                Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                                IconButton(onClick = { viewModel.updateQuantity(item.product.id, item.quantity + 1) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Больше")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { viewModel.removeFromCart(item.product.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
