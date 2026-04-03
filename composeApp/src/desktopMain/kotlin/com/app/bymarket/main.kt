package com.app.bymarket

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.app.bymarket.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ByMarket",
        ) {
            App()
        }
    }
}
