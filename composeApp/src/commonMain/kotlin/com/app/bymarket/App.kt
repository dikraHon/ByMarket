package com.app.bymarket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.bymarket.presentation.navigation.Screen
import com.app.bymarket.presentation.screens.*
import com.app.bymarket.presentation.vm.UserViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import bymarket.composeapp.generated.resources.Res
import bymarket.composeapp.generated.resources.icon_main_logo

val ScreenSaver = Saver<Screen, String>(
    save = {
        when (it) {
            is Screen.Splash -> "splash"
            is Screen.Auth -> "auth"
            is Screen.Login -> "login"
            is Screen.Registration -> "registration"
            is Screen.Main -> "main"
            is Screen.Profile -> "profile"
        }
    },
    restore = {
        when (it) {
            "auth" -> Screen.Auth
            "login" -> Screen.Login
            "registration" -> Screen.Registration
            "main" -> Screen.Main
            "profile" -> Screen.Profile
            else -> Screen.Splash
        }
    }
)

@Composable
fun App(
    userViewModel: UserViewModel = koinViewModel()
) {
    val user by userViewModel.user.collectAsState()
    val isLoadingUser by userViewModel.isLoading.collectAsState()
    
    var currentScreen by rememberSaveable(stateSaver = ScreenSaver) { 
        mutableStateOf(Screen.Splash)
    }

    LaunchedEffect(user, isLoadingUser) {
        if (!isLoadingUser) {
            if (user != null) {
                if (currentScreen is Screen.Splash || currentScreen is Screen.Auth || currentScreen is Screen.Login || currentScreen is Screen.Registration) {
                    currentScreen = Screen.Main
                }
            } else {
                if (currentScreen is Screen.Splash) {
                    currentScreen = Screen.Auth
                }
            }
        }
    }

    MaterialTheme {
        when (currentScreen) {
            Screen.Splash -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White), 
                    contentAlignment = Alignment.Center
                ) {
                    if (!getPlatform().name.contains("Android", ignoreCase = true)) {
                        Image(
                            painter = painterResource(Res.drawable.icon_main_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(288.dp)
                        )
                    }
                }
            }
            Screen.Auth -> AuthScreen(
                onNavigateToLogin = { currentScreen = Screen.Login },
                onNavigateToRegistration = { currentScreen = Screen.Registration }
            )
            Screen.Login -> LoginScreen(
                onNavigateBack = { currentScreen = Screen.Auth },
                onNavigateToMain = { currentScreen = Screen.Main }
            )
            Screen.Registration -> RegistrationScreen(
                onNavigateBack = { currentScreen = Screen.Auth },
                onNavigateToMain = { currentScreen = Screen.Main }
            )
            Screen.Main -> MainScreen(
                onNavigateToProfile = { currentScreen = Screen.Profile }
            )
            Screen.Profile -> ProfileScreen(
                onNavigateBack = { currentScreen = Screen.Main },
                onLogout = {
                    userViewModel.logout()
                    currentScreen = Screen.Auth
                }
            )
        }
    }
}
