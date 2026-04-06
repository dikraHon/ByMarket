package com.app.bymarket.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.bymarket.data.local.entities.PackPriceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PackPriceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prices: List<PackPriceEntity>)

    @Query("SELECT * FROM pack_price WHERE pack_id = :packId")
    fun getPricesByPackId(packId: Int): Flow<List<PackPriceEntity>>
}
