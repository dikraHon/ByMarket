package com.app.bymarket

import android.app.Application
import com.app.bymarket.di.initKoin
import org.koin.android.ext.koin.androidContext

class MarketApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MarketApp)
        }
    }
}
