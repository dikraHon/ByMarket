package com.app.bymarket

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform