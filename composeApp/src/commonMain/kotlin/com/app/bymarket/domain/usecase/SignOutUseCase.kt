package com.app.bymarket.domain.usecase

import com.app.bymarket.domain.repository.AuthRepository

class SignOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() {
        repository.signOut()
    }
}
