package com.app.bymarket.presentation.vm.userVm

sealed class UserEffect {
    data class Error(val message: String) : UserEffect()
    object LogoutSuccess : UserEffect()
}
