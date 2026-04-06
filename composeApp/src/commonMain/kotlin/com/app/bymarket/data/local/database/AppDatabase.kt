package com.app.bymarket.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.app.bymarket.data.local.dao.BarcodeDao
import com.app.bymarket.data.local.dao.PackDao
import com.app.bymarket.data.local.dao.PackPriceDao
import com.app.bymarket.data.local.dao.UnitDao
import com.app.bymarket.data.local.entities.BarcodeEntity
import com.app.bymarket.data.local.entities.PackEntity
import com.app.bymarket.data.local.entities.PackPriceEntity
import com.app.bymarket.data.local.entities.UnitEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [
        UnitEntity::class,
        PackEntity::class,
        BarcodeEntity::class,
        PackPriceEntity::class
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun unitDao(): UnitDao
    abstract fun packDao(): PackDao
    abstract fun barcodeDao(): BarcodeDao
    abstract fun packPriceDao(): PackPriceDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

internal const val DB_NAME = "bymarket.db"

fun getDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
