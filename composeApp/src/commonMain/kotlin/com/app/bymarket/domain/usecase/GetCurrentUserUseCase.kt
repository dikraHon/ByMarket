package com.app.bymarket.domain.usecase

import com.app.bymarket.domain.models.userModels.User
import com.app.bymarket.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): User? {
        return repository.getCurrentUser()
    }
}
