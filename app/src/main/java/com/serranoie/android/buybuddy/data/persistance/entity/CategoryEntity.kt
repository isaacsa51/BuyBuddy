package com.serranoie.android.buybuddy.data.persistance.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.serranoie.android.buybuddy.data.util.Converters

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "categoryId") val categoryId: Int?,
    @ColumnInfo(name = "name") val name: String,
)

@Entity
@TypeConverters(Converters::class)
data class MonthlySumCategoryEntityStatusZero(
    val categoryName: String?,
    val totalPrice: List<Double>?
)

data class MonthlySumCategoryEntityStatusOne(
    val categoryName: String?,
    val totalPrice: List<Double>?
)