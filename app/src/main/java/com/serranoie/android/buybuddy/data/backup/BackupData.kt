package com.serranoie.android.buybuddy.data.backup

import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity

data class BackupData(
    val categories: List<CategoryEntity>,
    val items: List<ItemEntity>,
    val categoriesWithItems: List<CategoryWithItemsEntity>
)