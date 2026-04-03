package com.app.bymarket.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Auth : Screen()
    @Serializable
    data object Login : Screen()
    @Serializable
    data object Registration : Screen()
    @Serializable
    data object Main : Screen()
    @Serializable
    data object Profile : Screen()
}
