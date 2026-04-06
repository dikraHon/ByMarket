package com.app.bymarket.util

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics

actual object CrashLogger {
    actual fun recordException(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)
    }

    actual fun log(message: String) {
        Firebase.crashlytics.log(message)
    }
}
