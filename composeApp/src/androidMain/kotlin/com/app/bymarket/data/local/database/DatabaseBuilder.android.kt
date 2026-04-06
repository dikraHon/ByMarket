package com.app.bymarket.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.bymarket.MarketApp

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = MarketApp.INSTANCE.applicationContext
    val dbFile = appContext.getDatabasePath(DB_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
