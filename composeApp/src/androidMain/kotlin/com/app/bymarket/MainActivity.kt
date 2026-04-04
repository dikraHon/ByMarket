package com.app.bymarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.bymarket.presentation.vm.UserViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    
    private val userViewModel: UserViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            userViewModel.isLoading.value
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        setContent {
            App(userViewModel = userViewModel)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}