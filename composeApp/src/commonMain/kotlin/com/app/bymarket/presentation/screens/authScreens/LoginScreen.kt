package com.app.bymarket.presentation.screens.authScreens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.bymarket.presentation.vm.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMain: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    
    val isLoading by viewModel.isLoading.collectAsState()
    val apiError by viewModel.error.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState(initial = false)

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onNavigateToMain()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Вход", style = MaterialTheme.typography.headlineLarge)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        EmailField(
            value = email,
            onValueChange = { viewModel.onEmailChanged(it) },
            error = emailError
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        PasswordField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            error = passwordError
        )
        
        apiError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { viewModel.login() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && emailError == null && passwordError == null && email.isNotEmpty()
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else Text("Войти")
        }
        
        TextButton(onClick = onNavigateBack) {
            Text("Назад")
        }
    }
}
