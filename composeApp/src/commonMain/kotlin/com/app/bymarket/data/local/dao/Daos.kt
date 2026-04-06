package com.app.bymarket.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.bymarket.data.local.entities.BarcodeEntity
import com.app.bymarket.data.local.entities.PackEntity
import com.app.bymarket.data.local.entities.PackPriceEntity
import com.app.bymarket.data.local.entities.UnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(units: List<UnitEntity>)

    @Query("SELECT * FROM unit")
    fun getAllUnits(): Flow<List<UnitEntity>>
}

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

@Dao
interface BarcodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(barcodes: List<BarcodeEntity>)

    @Query("SELECT * FROM barcode WHERE pack_id = :packId")
    fun getBarcodesByPackId(packId: Int): Flow<List<BarcodeEntity>>
    
    @Query("SELECT * FROM barcode WHERE body = :barcode")
    suspend fun getBarcodeByBody(barcode: String): BarcodeEntity?
}

@Dao
interface PackPriceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prices: List<PackPriceEntity>)

    @Query("SELECT * FROM pack_price WHERE pack_id = :packId")
    fun getPricesByPackId(packId: Int): Flow<List<PackPriceEntity>>
}
