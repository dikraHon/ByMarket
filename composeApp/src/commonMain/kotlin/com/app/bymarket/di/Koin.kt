package com.app.bymarket.di

import com.app.bymarket.di.modules.*
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            dataModule,
            repositoryModule,
            domainModule,
            viewModelModule
        )
    }
