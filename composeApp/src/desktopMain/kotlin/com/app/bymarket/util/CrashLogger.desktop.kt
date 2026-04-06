package com.app.bymarket.util

actual object CrashLogger {
    actual fun recordException(throwable: Throwable) {
        println("Desktop Error: ${throwable.message}")
        throwable.printStackTrace()
    }

    actual fun log(message: String) {
        println("Desktop Log: $message")
    }
}
