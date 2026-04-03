package com.app.bymarket.domain.usecase

import com.app.bymarket.domain.models.userModels.RegistrationData
import com.app.bymarket.domain.models.userModels.User
import com.app.bymarket.domain.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(registrationData: RegistrationData): Result<User> {
        return repository.signUp(registrationData)
    }
}
