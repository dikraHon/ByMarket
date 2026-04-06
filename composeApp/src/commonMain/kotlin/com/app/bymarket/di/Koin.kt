package com.app.bymarket.di

import com.app.bymarket.data.local.database.AppDatabase
import com.app.bymarket.data.local.database.getDatabase
import com.app.bymarket.data.local.database.getDatabaseBuilder
import com.app.bymarket.data.repository.AuthRepositoryImpl
import com.app.bymarket.data.repository.ProductRepositoryImpl
import com.app.bymarket.domain.repository.AuthRepository
import com.app.bymarket.domain.repository.ProductRepository
import com.app.bymarket.domain.usecase.GetCurrentUserUseCase
import com.app.bymarket.domain.usecase.SignInUseCase
import com.app.bymarket.domain.usecase.SignOutUseCase
import com.app.bymarket.domain.usecase.SignUpUseCase
import com.app.bymarket.presentation.vm.AuthViewModel
import com.app.bymarket.presentation.vm.CartViewModel
import com.app.bymarket.presentation.vm.ProductViewModel
import com.app.bymarket.presentation.vm.UserViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val firebaseModule = module {
    single {
        Firebase.auth 
    }
    single { Firebase.firestore }
}

val databaseModule = module {
    single<AppDatabase> { getDatabase(getDatabaseBuilder()) }
    single { get<AppDatabase>().unitDao() }
    single { get<AppDatabase>().packDao() }
    single { get<AppDatabase>().barcodeDao() }
    single { get<AppDatabase>().packPriceDao() }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get(), get(), get()) }
    single<com.app.bymarket.domain.repository.PurchaseRepository> { com.app.bymarket.data.repository.PurchaseRepositoryImpl(get()) }
}

val useCaseModule = module {
    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { SignOutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
}

val viewModelModule = module {
    factory { AuthViewModel(get(), get()) }
    factory { UserViewModel(get(), get(), get()) }
    factory { ProductViewModel(get()) }
    single { CartViewModel(get(), get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(firebaseModule, databaseModule, repositoryModule, useCaseModule, viewModelModule)
    }
