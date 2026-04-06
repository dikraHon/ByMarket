package com.app.bymarket.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.bymarket.data.local.entities.BarcodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(barcodes: List<BarcodeEntity>)

    @Query("SELECT * FROM barcode WHERE pack_id = :packId")
    fun getBarcodesByPackId(packId: Int): Flow<List<BarcodeEntity>>

    @Query("SELECT * FROM barcode WHERE body = :barcode")
    suspend fun getBarcodeByBody(barcode: String): BarcodeEntity?
}