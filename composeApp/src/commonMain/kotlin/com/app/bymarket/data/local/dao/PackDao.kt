package com.app.bymarket.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.bymarket.data.local.entities.PackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(packs: List<PackEntity>)

    @Query("SELECT * FROM pack")
    fun getAllPacks(): Flow<List<PackEntity>>

    @Query("SELECT * FROM pack WHERE id = :id")
    suspend fun getPackById(id: Int): PackEntity?

    @Query("UPDATE pack SET quant = quant - :amount WHERE id = :id")
    suspend fun reduceQuantity(id: Int, amount: Double)

    @Query("SELECT pack.* FROM pack INNER JOIN barcode ON pack.id = barcode.pack_id WHERE barcode.body = :barcode")
    suspend fun getPackByBarcode(barcode: String): PackEntity?
}