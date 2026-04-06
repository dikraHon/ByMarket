package com.app.bymarket.util

expect object CrashLogger {
    fun recordException(throwable: Throwable)
    fun log(message: String)
}
