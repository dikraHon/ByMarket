package com.app.bymarket.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Navigator(initialStack: List<Screen>) {
    constructor(initialScreen: Screen) : this(listOf(initialScreen))

    private var _stack by mutableStateOf(initialStack)

    val currentScreen: Screen
        get() = _stack.last()

    val canGoBack: Boolean
        get() = _stack.size > 1

    fun navigateTo(screen: Screen) {
        _stack = _stack + screen
    }

    fun replaceTo(screen: Screen) {
        _stack = listOf(screen)
    }

    fun pop() {
        if (canGoBack) {
            _stack = _stack.dropLast(1)
        }
    }

    fun getStack(): List<Screen> = _stack
}
