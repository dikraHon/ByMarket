package com.app.bymarket.presentation.vm.userVm

import com.app.bymarket.domain.models.userModels.User
import com.app.bymarket.domain.models.purchaseModels.Purchase

data class UserState(
    val user: User? = null,
    val isLoading: Boolean = true,
    val purchaseHistory: List<Purchase> = emptyList()
)
