package com.app.bymarket

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.app.bymarket.presentation.navigation.Screen
import com.app.bymarket.presentation.screens.*
import com.app.bymarket.presentation.vm.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    userViewModel: UserViewModel = koinViewModel()
) {
    val user by userViewModel.user.collectAsState()
    val isLoadingUser by userViewModel.isLoading.collectAsState()
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Auth) }
    LaunchedEffect(user, isLoadingUser) {
        if (!isLoadingUser && user != null) {
            if (currentScreen is Screen.Auth || currentScreen is Screen.Login || currentScreen is Screen.Registration) {
                currentScreen = Screen.Main
            }
        }
    }

    MaterialTheme {
        if (isLoadingUser && user == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            when (currentScreen) {
                is Screen.Auth -> AuthScreen(
                    onNavigateToLogin = { currentScreen = Screen.Login },
                    onNavigateToRegistration = { currentScreen = Screen.Registration }
                )
                is Screen.Login -> LoginScreen(
                    onNavigateBack = { currentScreen = Screen.Auth },
                    onNavigateToMain = { currentScreen = Screen.Main }
                )
                is Screen.Registration -> RegistrationScreen(
                    onNavigateBack = { currentScreen = Screen.Auth },
                    onNavigateToMain = { currentScreen = Screen.Main }
                )
                is Screen.Main -> MainScreen(
                    onNavigateToProfile = { currentScreen = Screen.Profile }
                )
                is Screen.Profile -> ProfileScreen(
                    onNavigateBack = { currentScreen = Screen.Main },
                    onLogout = {
                        userViewModel.logout()
                        currentScreen = Screen.Auth
                    }
                )
            }
        }
    }
}
