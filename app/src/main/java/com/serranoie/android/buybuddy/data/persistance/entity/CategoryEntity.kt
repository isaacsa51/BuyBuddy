package com.serranoie.android.buybuddy.data.persistance.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryId") val categoryId: Int?,
    @ColumnInfo(name = "name") val name: String,
)

data class MonthlySumCategoryEntityStatusZero(
    val categoryName: String?,
    val totalPrice: Double?
)

data class MonthlySumCategoryEntityStatusOne(
    val categoryName: String?,
    val totalPrice: Double?
)