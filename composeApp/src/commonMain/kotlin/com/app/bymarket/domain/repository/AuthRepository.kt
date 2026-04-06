package com.app.bymarket.domain.repository

import com.app.bymarket.domain.models.userModels.LoginData
import com.app.bymarket.domain.models.userModels.RegistrationData
import com.app.bymarket.domain.models.userModels.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<User?>
    suspend fun signIn(loginData: LoginData): Result<User>
    suspend fun signUp(registrationData: RegistrationData): Result<User>
    suspend fun signOut()
    suspend fun getCurrentUser(): User?
}
