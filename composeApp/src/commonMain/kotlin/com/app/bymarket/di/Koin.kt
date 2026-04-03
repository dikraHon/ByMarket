package com.app.bymarket.di

import com.app.bymarket.data.repository.AuthRepositoryImpl
import com.app.bymarket.domain.repository.AuthRepository
import com.app.bymarket.domain.usecase.GetCurrentUserUseCase
import com.app.bymarket.domain.usecase.SignInUseCase
import com.app.bymarket.domain.usecase.SignOutUseCase
import com.app.bymarket.domain.usecase.SignUpUseCase
import com.app.bymarket.presentation.vm.AuthViewModel
import com.app.bymarket.presentation.vm.UserViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val firebaseModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { SignOutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
}

val viewModelModule = module {
    factory { AuthViewModel(get(), get()) }
    factory { UserViewModel(get(), get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(firebaseModule, repositoryModule, useCaseModule, viewModelModule)
    }
