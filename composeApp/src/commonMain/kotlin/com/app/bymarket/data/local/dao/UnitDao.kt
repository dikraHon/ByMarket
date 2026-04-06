package com.app.bymarket.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.bymarket.data.local.entities.UnitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(units: List<UnitEntity>)

    @Query("SELECT * FROM unit")
    fun getAllUnits(): Flow<List<UnitEntity>>
}