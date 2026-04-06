package com.app.bymarket.domain.usecase

import com.app.bymarket.domain.models.userModels.LoginData
import com.app.bymarket.domain.models.userModels.User
import com.app.bymarket.domain.repository.AuthRepository
import com.app.bymarket.domain.validation.AuthValidator
import com.app.bymarket.domain.validation.AuthValidationError
import com.app.bymarket.domain.validation.ValidationResult

class SignInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(loginData: LoginData): Result<User> {
        val emailRes = AuthValidator.validateEmail(loginData.email)
        val passRes = AuthValidator.validatePassword(loginData.password)

        if (emailRes is ValidationResult.Error || passRes is ValidationResult.Error) {
            return Result.failure(
                AuthValidationError(
                    emailError = (emailRes as? ValidationResult.Error)?.message,
                    passwordError = (passRes as? ValidationResult.Error)?.message
                )
            )
        }

        return repository.signIn(loginData)
    }
}
