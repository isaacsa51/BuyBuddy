package com.serranoie.android.buybuddy.data.persistance.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithItemsEntity(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val items: List<ItemEntity>
)