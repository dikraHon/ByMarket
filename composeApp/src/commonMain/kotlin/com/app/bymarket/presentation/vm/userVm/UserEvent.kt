package com.app.bymarket.presentation.vm.userVm

sealed class UserEvent {
    object Logout : UserEvent()
    object RefreshHistory : UserEvent()
}
