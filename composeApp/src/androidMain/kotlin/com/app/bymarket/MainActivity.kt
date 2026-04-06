package com.app.bymarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.bymarket.presentation.vm.userVm.UserViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    
    private val userViewModel: UserViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        splashScreen.setKeepOnScreenCondition {
            userViewModel.state.value.isLoading
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        setContent {
            App(userViewModel = userViewModel)
        }
    }
}
