package com.app.bymarket.domain.usecase

import com.app.bymarket.domain.models.userModels.RegistrationData
import com.app.bymarket.domain.models.userModels.User
import com.app.bymarket.domain.repository.AuthRepository
import com.app.bymarket.domain.validation.AuthValidator
import com.app.bymarket.domain.validation.AuthValidationError
import com.app.bymarket.domain.validation.ValidationResult

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(registrationData: RegistrationData): Result<User> {
        val emailRes = AuthValidator.validateEmail(registrationData.email)
        val passRes = AuthValidator.validatePassword(registrationData.password)
        val firstRes = AuthValidator.validateName(registrationData.firstName, "имя")
        val lastRes = AuthValidator.validateName(registrationData.lastName, "фамилию")

        if (emailRes is ValidationResult.Error || 
            passRes is ValidationResult.Error ||
            firstRes is ValidationResult.Error ||
            lastRes is ValidationResult.Error
        ) {
            return Result.failure(
                AuthValidationError(
                    emailError = (emailRes as? ValidationResult.Error)?.message,
                    passwordError = (passRes as? ValidationResult.Error)?.message,
                    firstNameError = (firstRes as? ValidationResult.Error)?.message,
                    lastNameError = (lastRes as? ValidationResult.Error)?.message
                )
            )
        }

        return repository.signUp(registrationData)
    }
}
