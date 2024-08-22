package com.serranoie.android.buybuddy.domain.repository

import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.model.CategoryWithItems
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusOne
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusZero
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategoriesWithItems(): Flow<List<CategoryWithItemsEntity>>
    suspend fun getCategoryById(categoryId: Int): Flow<Category>
    suspend fun getCategorySummaryWithStatusZero(): Flow<List<MonthlySumCategoryStatusZero>?>
    suspend fun getCategorySummaryWithStatusOne(): Flow<List<MonthlySumCategoryStatusOne>?>
}