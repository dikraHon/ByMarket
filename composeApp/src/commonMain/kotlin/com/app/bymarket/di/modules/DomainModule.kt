package com.app.bymarket.di.modules

import com.app.bymarket.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    factory { SignInUseCase(get()) }
    factory { SignUpUseCase(get()) }
    factory { SignOutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
}
