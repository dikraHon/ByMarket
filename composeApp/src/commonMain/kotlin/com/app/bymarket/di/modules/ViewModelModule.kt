package com.app.bymarket.di.modules

import com.app.bymarket.presentation.vm.authVm.AuthViewModel
import com.app.bymarket.presentation.vm.cartVm.CartViewModel
import com.app.bymarket.presentation.vm.productVm.ProductViewModel
import com.app.bymarket.presentation.vm.userVm.UserViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { AuthViewModel(get(), get()) }
    factory { UserViewModel(get(), get(), get()) }
    factory { ProductViewModel(get()) }
    single { CartViewModel(get(), get()) }
}
