package com.app.bymarket.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = NSHomeDirectory() + "/$DB_NAME"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile,
        factory = { AppDatabaseConstructor.initialize() }
    )
}
