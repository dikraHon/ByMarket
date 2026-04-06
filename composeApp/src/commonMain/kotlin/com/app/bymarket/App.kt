package com.app.bymarket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.bymarket.presentation.navigation.Screen
import com.app.bymarket.presentation.navigation.Navigator
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import bymarket.composeapp.generated.resources.Res
import bymarket.composeapp.generated.resources.icon_main_logo
import com.app.bymarket.presentation.screens.authScreens.AuthScreen
import com.app.bymarket.presentation.screens.authScreens.LoginScreen
import com.app.bymarket.presentation.screens.authScreens.RegistrationScreen
import com.app.bymarket.presentation.screens.cartScreens.CartScreen
import com.app.bymarket.presentation.screens.mainScreens.MainScreen
import com.app.bymarket.presentation.screens.profileScreens.ProfileScreen
import com.app.bymarket.presentation.vm.cartVm.CartViewModel
import com.app.bymarket.presentation.vm.productVm.ProductViewModel
import com.app.bymarket.presentation.vm.userVm.UserViewModel

val NavigatorSaver: Saver<Navigator, Any> = listSaver(
    save = { navigator -> 
        navigator.getStack().map { screen ->
            when (screen) {
                is Screen.Splash -> "splash"
                is Screen.Auth -> "auth"
                is Screen.Login -> "login"
                is Screen.Registration -> "registration"
                is Screen.Main -> "main"
                is Screen.Profile -> "profile"
                is Screen.Cart -> "cart"
            }
        }
    },
    restore = { savedList ->
        val screens = savedList.map {
            when (it) {
                "auth" -> Screen.Auth
                "login" -> Screen.Login
                "registration" -> Screen.Registration
                "main" -> Screen.Main
                "profile" -> Screen.Profile
                "cart" -> Screen.Cart
                else -> Screen.Splash
            }
        }
        Navigator(screens)
    }
)

@Composable
fun App(
    userViewModel: UserViewModel = koinViewModel()
) {
    val state by userViewModel.state.collectAsState()
    
    val navigator = rememberSaveable(saver = NavigatorSaver) { 
        Navigator(Screen.Splash)
    }

    LaunchedEffect(state.user, state.isLoading) {
        if (!state.isLoading) {
            val current = navigator.currentScreen
            if (state.user != null) {
                if (current is Screen.Splash || current is Screen.Auth || current is Screen.Login || current is Screen.Registration) {
                    navigator.replaceTo(Screen.Main)
                }
            } else {
                if (current is Screen.Splash) {
                    navigator.replaceTo(Screen.Auth)
                }
            }
        }
    }

    MaterialTheme {
        when (navigator.currentScreen) {
            Screen.Splash -> {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.White), 
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
                onNavigateToLogin = { navigator.navigateTo(Screen.Login) },
                onNavigateToRegistration = { navigator.navigateTo(Screen.Registration) }
            )
            Screen.Login -> LoginScreen(
                onNavigateBack = { navigator.pop() },
                onNavigateToMain = { navigator.replaceTo(Screen.Main) }
            )
            Screen.Registration -> RegistrationScreen(
                onNavigateBack = { navigator.pop() },
                onNavigateToMain = { navigator.replaceTo(Screen.Main) }
            )
            Screen.Main -> MainScreen(
                viewModel = koinViewModel<ProductViewModel>(),
                cartViewModel = koinViewModel<CartViewModel>(),
                onNavigateToProfile = { navigator.navigateTo(Screen.Profile) },
                onNavigateToCart = { navigator.navigateTo(Screen.Cart) }
            )
            Screen.Cart -> CartScreen(
                viewModel = koinViewModel<CartViewModel>(),
                userViewModel = userViewModel,
                onNavigateBack = { navigator.pop() }
            )
            Screen.Profile -> ProfileScreen(
                onNavigateBack = { navigator.pop() },
                onLogoutSuccess = {
                    navigator.replaceTo(Screen.Auth)
                },
                viewModel = userViewModel
            )
        }
    }
}
