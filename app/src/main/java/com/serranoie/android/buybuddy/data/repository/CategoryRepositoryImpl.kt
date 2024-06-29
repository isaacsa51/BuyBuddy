package com.serranoie.android.buybuddy.data.repository

import com.serranoie.android.buybuddy.data.mapper.toDomain
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.model.CategoryWithItems
import com.serranoie.android.buybuddy.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val buyBuddyDao: BuyBuddyDao) :
    CategoryRepository {

    override fun getCategoriesWithItems(): Flow<List<CategoryWithItemsEntity>> =
        buyBuddyDao.getCategoriesWithItems()

    override suspend fun getCategoryById(categoryId: Int): Flow<Category> =
        buyBuddyDao.getCategoryById(categoryId)
}