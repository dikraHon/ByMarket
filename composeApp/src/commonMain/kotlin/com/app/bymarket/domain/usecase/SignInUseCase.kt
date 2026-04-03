package com.app.bymarket.domain.usecase

import com.app.bymarket.domain.models.userModels.LoginData
import com.app.bymarket.domain.models.userModels.User
import com.app.bymarket.domain.repository.AuthRepository

class SignInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(loginData: LoginData): Result<User> {
        return repository.signIn(loginData)
    }
}
