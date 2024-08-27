package com.serranoie.android.buybuddy.data.persistance.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "item",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "itemId") val itemId: Int?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "categoryId") val categoryId: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "usage") val usage: String,
    @ColumnInfo(name = "benefits") val benefits: String,
    @ColumnInfo(name = "disadvantages") val disadvantages: String?,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "reminderDate") val reminderDate: Date?,
    @ColumnInfo(name = "reminderTime") val reminderTime: Date?,
    @ColumnInfo(name = "status") val status: Boolean = false,
)

data class ItemPriceEntityStatusZero(val price: Double?)

data class ItemPriceEntityStatusOne(val price: Double?)

data class MonthlySumEntityStatusZero(
    val month: String?,
    val totalSum: Double?
)

data class MonthlySumEntityStatusOne(
    val month: String?,
    val totalSum: Double?
)