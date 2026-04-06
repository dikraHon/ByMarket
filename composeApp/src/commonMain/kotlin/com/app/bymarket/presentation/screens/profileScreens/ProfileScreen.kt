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
import com.app.bymarket.domain.models.purchaseModels.Purchase
import com.app.bymarket.presentation.vm.userVm.UserEffect
import com.app.bymarket.presentation.vm.userVm.UserEvent
import com.app.bymarket.presentation.vm.userVm.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogoutSuccess: () -> Unit,
    viewModel: UserViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val selectedPurchase = remember { mutableStateOf<Purchase?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is UserEffect.LogoutSuccess -> onLogoutSuccess()
                is UserEffect.Error -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
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
            if (state.isLoading && state.user == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val user = state.user
                val fullName = buildString {
                    append(user?.lastName ?: "")
                    append(" ")
                    append(user?.firstName ?: "")
                    user?.patronymic?.let { 
                        append(" ")
                        append(it)
                    }
                }.trim().ifEmpty { "Пользователь" }

                Text(
                    text = fullName,
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

                if (state.purchaseHistory.isEmpty()) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (state.isLoading) "Загрузка..." else "История пока пуста", 
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        items(state.purchaseHistory) { purchase ->
                            PurchaseHistoryItem(
                                purchase = purchase,
                                onClick = { selectedPurchase.value = purchase }
                            )
                        }
                    }
                }
                
                Button(
                    onClick = { viewModel.onEvent(UserEvent.Logout) },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Выйти из аккаунта")
                }
            }
        }
    }

    selectedPurchase.value?.let { purchase ->
        PurchaseDetailDialog(purchase) { 
            selectedPurchase.value = null 
        }
    }
}
