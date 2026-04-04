package com.app.bymarket

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.app.bymarket.di.initKoin
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

fun main() {
    try {
        Firebase.initialize(
            options = FirebaseOptions(
                applicationId = "1:459455836503:android:f857e7d38d6b14f40ace12",
                apiKey = "AIzaSyA32AhFyNbijcFKVZWb0Ri7tFumc-O9pVo",
                projectId = "by-market-bc9c5",
                storageBucket = "by-market-bc9c5.firebasestorage.app"
            )
        )

        initKoin()
        
        application {
            Window(
                onCloseRequest = ::exitApplication,
                title = "ByMarket",
            ) {
                App()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
