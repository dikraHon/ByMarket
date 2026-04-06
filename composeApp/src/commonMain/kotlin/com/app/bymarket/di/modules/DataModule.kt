package com.app.bymarket.di.modules

import com.app.bymarket.data.local.database.AppDatabase
import com.app.bymarket.data.local.database.getDatabase
import com.app.bymarket.data.local.database.getDatabaseBuilder
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.dsl.module

val dataModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
    single<AppDatabase> { getDatabase(getDatabaseBuilder()) }
    single { get<AppDatabase>().unitDao() }
    single { get<AppDatabase>().packDao() }
    single { get<AppDatabase>().barcodeDao() }
    single { get<AppDatabase>().packPriceDao() }
}
