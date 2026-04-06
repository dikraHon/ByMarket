package com.app.bymarket.presentation.screens.authScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.bymarket.presentation.vm.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegistrationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMain: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val firstName by viewModel.firstName.collectAsState()
    val lastName by viewModel.lastName.collectAsState()
    val patronymic by viewModel.patronymic.collectAsState()
    
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val firstNameError by viewModel.firstNameError.collectAsState()
    val lastNameError by viewModel.lastNameError.collectAsState()
    
    val isLoading by viewModel.isLoading.collectAsState()
    val apiError by viewModel.error.collectAsState()
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
            value = lastName,
            onValueChange = { viewModel.onLastNameChanged(it) },
            label = "Фамилия",
            error = lastNameError
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        NameField(
            value = firstName,
            onValueChange = { viewModel.onFirstNameChanged(it) },
            label = "Имя",
            error = firstNameError
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        NameField(
            value = patronymic,
            onValueChange = { viewModel.onPatronymicChanged(it) },
            label = "Отчество (необязательно)"
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
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
            onClick = { viewModel.signUp() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && 
                     emailError == null && 
                     passwordError == null && 
                     firstNameError == null && 
                     lastNameError == null &&
                     email.isNotEmpty() &&
                     password.isNotEmpty()
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else Text("Зарегистрироваться")
        }
        
        TextButton(onClick = onNavigateBack) {
            Text("Назад")
        }
    }
}
