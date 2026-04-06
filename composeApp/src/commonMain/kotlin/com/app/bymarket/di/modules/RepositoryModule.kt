package com.app.bymarket.di.modules

import com.app.bymarket.data.repository.AuthRepositoryImpl
import com.app.bymarket.data.repository.ProductRepositoryImpl
import com.app.bymarket.data.repository.PurchaseRepositoryImpl
import com.app.bymarket.domain.repository.AuthRepository
import com.app.bymarket.domain.repository.ProductRepository
import com.app.bymarket.domain.repository.PurchaseRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get(), get(), get()) }
    single<PurchaseRepository> { PurchaseRepositoryImpl(get()) }
}
