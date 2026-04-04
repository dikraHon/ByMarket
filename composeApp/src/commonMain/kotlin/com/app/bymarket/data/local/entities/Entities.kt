package com.app.bymarket.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "unit"
)
data class UnitEntity(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(
    tableName = "pack",
    foreignKeys = [
        ForeignKey(
            entity = UnitEntity::class,
            parentColumns = ["id"],
            childColumns = ["unit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PackEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "unit_id") val unitId: Int,
    val name: String,
    val type: Int,
    val quant: Int,
    val stock: Double = 0.0
)

@Entity(
    tableName = "barcode",
    foreignKeys = [
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["pack_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BarcodeEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "pack_id") val packId: Int,
    val body: String
)

@Entity(
    tableName = "pack_price",
    foreignKeys = [
        ForeignKey(
            entity = PackEntity::class,
            parentColumns = ["id"],
            childColumns = ["pack_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PackPriceEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "pack_id") val packId: Int,
    val price: Int,
    val bonus: Int
)
