package com.app.bymarket.presentation.vm.authVm

sealed class AuthEvent {
    data class EmailChanged(val value: String) : AuthEvent()
    data class PasswordChanged(val value: String) : AuthEvent()
    data class FirstNameChanged(val value: String) : AuthEvent()
    data class LastNameChanged(val value: String) : AuthEvent()
    data class PatronymicChanged(val value: String) : AuthEvent()
    object LoginClicked : AuthEvent()
    object SignUpClicked : AuthEvent()
    object ErrorDismissed : AuthEvent()
}
