package com.app.bymarket.di.modules

import com.app.bymarket.presentation.vm.CartViewModel
import com.app.bymarket.presentation.vm.ProductViewModel
import com.app.bymarket.presentation.vm.UserViewModel
import com.app.bymarket.presentation.vm.authVm.AuthViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { AuthViewModel(get(), get()) }
    factory { UserViewModel(get(), get(), get()) }
    factory { ProductViewModel(get()) }
    single { CartViewModel(get(), get()) }
}
