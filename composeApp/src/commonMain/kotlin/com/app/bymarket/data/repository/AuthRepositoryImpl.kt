package com.app.bymarket.data.repository

import com.app.bymarket.domain.models.userModels.LoginData
import com.app.bymarket.domain.models.userModels.RegistrationData
import com.app.bymarket.domain.models.userModels.User
import com.app.bymarket.domain.repository.AuthRepository
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.withTimeout

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signIn(loginData: LoginData): Result<User> = runCatching {
        val authResult = auth.signInWithEmailAndPassword(loginData.email, loginData.password)
        val uid = authResult.user?.uid ?: throw Exception("User ID not found")
        
        withTimeout(10000) {
            val userDoc = firestore.collection("users").document(uid).get()
            userDoc.data<User>()
        }
    }

    override suspend fun signUp(registrationData: RegistrationData): Result<User> = runCatching {
        println("Starting sign up for ${registrationData.email}")
        val authResult = auth.createUserWithEmailAndPassword(registrationData.email, registrationData.password)
        val uid = authResult.user?.uid ?: throw Exception("User creation failed")

        val newUser = User(
            uid = uid,
            email = registrationData.email,
            firstName = registrationData.firstName,
            lastName = registrationData.lastName,
            patronymic = registrationData.patronymic
        )

        println("Auth success, saving to Firestore for uid: $uid")

        try {
            withTimeout(10000) {
                firestore.collection("users").document(uid).set(newUser)
            }
            println("Firestore write success")
        } catch (e: Exception) {
            println("Firestore write failed: ${e.message}")
            throw Exception("Ошибка сохранения профиля: ${e.message}. Проверьте правила доступа в консоли Firebase.")
        }
        
        newUser
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            withTimeout(5000) {
                firestore.collection("users").document(uid).get().data<User>()
            }
        } catch (_: Exception) {
            null
        }
    }
}
