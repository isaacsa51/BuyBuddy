package com.serranoie.android.buybuddy.data.repository

import com.serranoie.android.buybuddy.data.mapper.toDomain
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusOne
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusZero
import com.serranoie.android.buybuddy.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val buyBuddyDao: BuyBuddyDao) :
    CategoryRepository {

    override fun getCategoriesWithItems(): Flow<List<CategoryWithItemsEntity>> =
        buyBuddyDao.getCategoriesWithItems()

    override suspend fun getCategoryById(categoryId: Int): Flow<Category> =
        buyBuddyDao.getCategoryById(categoryId).map { it.toDomain() }

    override suspend fun getCategorySummaryWithStatusZero(month: String): Flow<List<MonthlySumCategoryStatusZero>?> =
        buyBuddyDao.getSummaryOfCategoryWithStatusZero(month).map { categoryPriceList ->
            categoryPriceList?.groupBy { it.categoryName }?.map { (categoryName, priceList) ->
                MonthlySumCategoryStatusZero(
                    categoryName,
                    priceList.flatMap { it.totalPrice ?: emptyList() }.toSet().toList()
                )
            }
        }

    override suspend fun getCategorySummaryWithStatusOne(month: String): Flow<List<MonthlySumCategoryStatusOne>?> =
        buyBuddyDao.getSummaryOfCategoryWithStatusOne(month).map { entities ->
            entities?.map { entity ->
                entity.toDomain()
            }
        }
}