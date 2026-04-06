package com.app.bymarket.presentation.screens.cartScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.bymarket.presentation.vm.CartViewModel
import com.app.bymarket.presentation.vm.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    userViewModel: UserViewModel,
    onNavigateBack: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val user by userViewModel.user.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.checkoutSuccess.collect {
            snackbarHostState.showSnackbar("Покупка успешно совершена!")
            onNavigateBack()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.error.collect {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Корзина") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
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
                        Button(onClick = { 
                            user?.let {
                                viewModel.checkout(it.uid)
                            } ?: scope.launch {
                                snackbarHostState.showSnackbar("Необходима авторизация")
                            }
                        }) {
                            Text("Оплатить")
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
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.product.name, fontWeight = FontWeight.Bold, maxLines = 1)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "${item.product.finalPrice} ₽", 
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        " x ${if (item.product.isWeight) item.quantity else item.quantity.toInt()} ${if (item.product.isWeight) "кг" else "шт"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                                Text(
                                    "Сумма: ${item.totalPrice} ₽",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val step = if (item.product.isWeight) 0.1 else 1.0
                                IconButton(onClick = { viewModel.updateQuantity(item.product.id, item.quantity - step) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Меньше")
                                }
                                Text(
                                    text = if (item.product.isWeight) {
                                        // Округляем для корректного отображения
                                        ((item.quantity * 10).toInt() / 10.0).toString()
                                    } else {
                                        item.quantity.toInt().toString()
                                    },
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                IconButton(onClick = { 
                                    viewModel.updateQuantity(item.product.id, item.quantity + step)
                                }) {
                                    Icon(Icons.Default.Add, contentDescription = "Больше")
                                }
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
