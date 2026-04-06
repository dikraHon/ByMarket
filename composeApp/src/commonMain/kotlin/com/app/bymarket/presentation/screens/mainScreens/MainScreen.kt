package com.app.bymarket.presentation.screens.mainScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.app.bymarket.domain.models.Product
import com.app.bymarket.presentation.components.BarcodeScanner
import com.app.bymarket.presentation.vm.CartViewModel
import com.app.bymarket.presentation.vm.ProductViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val products by viewModel.products.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var barcodeInput by remember { mutableStateOf("") }
    var isScanning by remember { mutableStateOf(false) }
    
    var selectedProductForAdd by remember { mutableStateOf<Product?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ByMarket") },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartItems.isNotEmpty()) {
                                Badge {
                                    Text(cartItems.sumOf { it.quantity }.toInt().toString())
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onNavigateToCart) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Корзина")
                        }
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Профиль")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = barcodeInput,
                    onValueChange = { barcodeInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    label = { Text("Штрихкод") },
                    placeholder = { Text("Введите или сканируйте") },
                    leadingIcon = { 
                        IconButton(onClick = { isScanning = true }) {
                            Icon(Icons.Default.QrCodeScanner, contentDescription = "Сканировать")
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Search
                    ),
                    trailingIcon = {
                        if (barcodeInput.isNotEmpty()) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        val product = viewModel.searchByBarcode(barcodeInput)
                                        if (product != null) {
                                            if (product.stock > 0) {
                                                selectedProductForAdd = product
                                                barcodeInput = ""
                                            } else {
                                                snackbarHostState.showSnackbar("Товара нет в наличии")
                                            }
                                        } else {
                                            snackbarHostState.showSnackbar("Товар не найден")
                                        }
                                    }
                                },
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Text("ОК")
                            }
                        }
                    }
                )

                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                onAddToCart = { selectedProductForAdd = it },
                                modifier = Modifier.clickable { selectedProductForAdd = product }
                            )
                        }
                    }
                }
            }

            selectedProductForAdd?.let { product ->
                AddToCartDialog(
                    product = product,
                    onDismiss = { selectedProductForAdd = null },
                    onConfirm = { quantity ->
                        cartViewModel.addToCart(product, quantity)
                        scope.launch {
                            snackbarHostState.showSnackbar("Добавлено: ${product.name} ($quantity)")
                        }
                        selectedProductForAdd = null
                    }
                )
            }

            if (isScanning) {
                BarcodeScanner(
                    onResult = { result ->
                        barcodeInput = result
                        isScanning = false
                        scope.launch {
                            val product = viewModel.searchByBarcode(result)
                            if (product != null && product.stock > 0) {
                                selectedProductForAdd = product
                                barcodeInput = ""
                            }
                        }
                    },
                    onClose = { isScanning = false },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
