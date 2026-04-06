package com.app.bymarket.presentation.screens.profileScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.bymarket.domain.models.Purchase
import com.app.bymarket.presentation.vm.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: UserViewModel = koinViewModel()
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val history by viewModel.purchaseHistory.collectAsState()
    val selectedPurchase = remember { mutableStateOf<Purchase?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профиль") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                val fullName = buildString {
                    append(user?.lastName ?: "")
                    append(" ")
                    append(user?.firstName ?: "")
                    user?.patronymic?.let { 
                        append(" ")
                        append(it)
                    }
                }.trim()

                Text(
                    text = fullName.ifEmpty { "Пользователь" },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user?.email ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    "История покупок",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.SemiBold
                )
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                if (history.isEmpty()) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text("История пока пуста", color = MaterialTheme.colorScheme.outline)
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        items(history) { purchase ->
                            PurchaseHistoryItem(purchase) {
                                selectedPurchase.value = purchase
                            }
                        }
                    }
                }
                
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Выйти из аккаунта")
                }
            }
        }
    }

    selectedPurchase.value?.let {
        PurchaseDetailDialog(it) { selectedPurchase.value = null }
    }
}