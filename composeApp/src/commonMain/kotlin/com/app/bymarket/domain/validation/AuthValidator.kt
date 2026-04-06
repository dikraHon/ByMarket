package com.app.bymarket.domain.validation

object AuthValidator {
    
    fun validateEmail(email: String): ValidationResult {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult.Error("Поле не может быть пустым")
            !trimmed.contains("@") -> ValidationResult.Error("Email должен содержать @")
            !trimmed.contains(".") -> ValidationResult.Error("Email должен содержать точку")
            trimmed.length < 5 -> ValidationResult.Error("Email слишком короткий")
            else -> ValidationResult.Success
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult.Error("Введите пароль")
            password.length < 6 -> ValidationResult.Error("Минимум 6 символов")
            else -> ValidationResult.Success
        }
    }

    fun validateName(name: String, fieldName: String): ValidationResult {
        val trimmed = name.trim()
        return when {
            trimmed.isEmpty() -> ValidationResult.Error("Введите $fieldName")
            trimmed.length < 2 -> ValidationResult.Error("Слишком короткое значение")
            else -> ValidationResult.Success
        }
    }
}
