package com.app.bymarket.presentation.screens.authScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.bymarket.presentation.screens.authScreens.components.EmailField
import com.app.bymarket.presentation.screens.authScreens.components.NameField
import com.app.bymarket.presentation.screens.authScreens.components.PasswordField
import com.app.bymarket.presentation.vm.authVm.AuthEvent
import com.app.bymarket.presentation.vm.authVm.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegistrationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMain: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState(initial = false)

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onNavigateToMain()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Регистрация", style = MaterialTheme.typography.headlineLarge)
        
        Spacer(modifier = Modifier.height(24.dp))

        NameField(
            value = state.lastName,
            onValueChange = { viewModel.onEvent(AuthEvent.LastNameChanged(it)) },
            label = "Фамилия",
            error = state.lastNameError
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        NameField(
            value = state.firstName,
            onValueChange = { viewModel.onEvent(AuthEvent.FirstNameChanged(it)) },
            label = "Имя",
            error = state.firstNameError
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        NameField(
            value = state.patronymic,
            onValueChange = { viewModel.onEvent(AuthEvent.PatronymicChanged(it)) },
            label = "Отчество (необязательно)"
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        EmailField(
            value = state.email,
            onValueChange = { viewModel.onEvent(AuthEvent.EmailChanged(it)) },
            error = state.emailError
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        PasswordField(
            value = state.password,
            onValueChange = { viewModel.onEvent(AuthEvent.PasswordChanged(it)) },
            error = state.passwordError
        )
        
        state.generalError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { viewModel.onEvent(AuthEvent.SignUpClicked) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else Text("Зарегистрироваться")
        }
        
        TextButton(onClick = onNavigateBack) {
            Text("Назад")
        }
    }
}
