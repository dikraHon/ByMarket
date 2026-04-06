package com.app.bymarket.presentation.screens.mainScreens

import androidx.compose.foundation.clickable
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
import com.app.bymarket.presentation.components.BarcodeScanner
import com.app.bymarket.presentation.vm.cartVm.CartEvent
import com.app.bymarket.presentation.vm.cartVm.CartViewModel
import com.app.bymarket.presentation.vm.productVm.ProductEvent
import com.app.bymarket.presentation.vm.productVm.ProductViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val cartState by cartViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ByMarket") },
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartState.items.isNotEmpty()) {
                                Badge {
                                    Text(cartState.totalQuantity.toString())
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
                    value = state.barcodeInput,
                    onValueChange = { viewModel.onEvent(ProductEvent.BarcodeInputChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    label = { Text("Штрихкод") },
                    placeholder = { Text("Введите или сканируйте") },
                    leadingIcon = { 
                        IconButton(onClick = { viewModel.onEvent(ProductEvent.StartScanning) }) {
                            Icon(Icons.Default.QrCodeScanner, contentDescription = "Сканировать")
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Search
                    ),
                    trailingIcon = {
                        if (state.barcodeInput.isNotEmpty()) {
                            Button(
                                onClick = { viewModel.onEvent(ProductEvent.SearchClicked) },
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Text("ОК")
                            }
                        }
                    }
                )

                if (state.isLoading && state.products.isEmpty()) {
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
                        items(state.products) { product ->
                            ProductCard(
                                product = product,
                                onAddToCart = { viewModel.onEvent(ProductEvent.ProductSelected(it)) },
                                modifier = Modifier.clickable { 
                                    viewModel.onEvent(ProductEvent.ProductSelected(product)) 
                                }
                            )
                        }
                    }
                }
            }

            state.selectedProductForAdd?.let { product ->
                AddToCartDialog(
                    product = product,
                    onDismiss = { viewModel.onEvent(ProductEvent.ProductSelected(null)) },
                    onConfirm = { quantity ->
                        cartViewModel.onEvent(CartEvent.AddToCart(product.id, quantity))
                        viewModel.onEvent(ProductEvent.ProductSelected(null))
                    }
                )
            }

            if (state.isScanning) {
                BarcodeScanner(
                    onResult = { result ->
                        viewModel.onEvent(ProductEvent.ScanResult(result))
                    },
                    onClose = { viewModel.onEvent(ProductEvent.StopScanning) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
