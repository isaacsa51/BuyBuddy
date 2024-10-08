package com.serranoie.android.buybuddy.data.repository

import com.serranoie.android.buybuddy.data.mapper.toDomain
import com.serranoie.android.buybuddy.data.mapper.toEntity
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusOne
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusZero
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusOne
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusZero
import com.serranoie.android.buybuddy.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(private val itemDao: BuyBuddyDao) : ItemRepository {

    override suspend fun insertItemWithCategory(item: Item, categoryName: String) =
        itemDao.insertItemWithCategory(item.toEntity(0), categoryName)

    override suspend fun insertItemAndReturnId(item: Item, categoryName: String): Int {
        val itemId = itemDao.insertItemWithCategoryAndReturnId(item.toEntity(0), categoryName)
        return itemId.toInt()
    }

    override suspend fun insertItem(item: Item) = itemDao.insertItem(item.toEntity(item.categoryId))

    override suspend fun updateItem(item: Item) = itemDao.updateItem(item.toEntity(item.categoryId))

    override suspend fun updateItemStatus(itemId: Int, status: Boolean) =
        itemDao.updateItemStatus(itemId, status)

    override suspend fun deleteItem(itemId: Int) = itemDao.deleteItem(itemId)

    override fun getAllItems(): Flow<List<Item>> =
        itemDao.getAllItems().map { entities -> entities.map { it.toDomain() } }

    override fun getItemById(itemId: Int): Flow<Item?> =
        itemDao.getItemById(itemId).map { it?.toDomain() }

    override suspend fun getTotalPriceOfItemsToBuy(): Flow<Double?> =
        itemDao.getTotalPriceOfItemsToBuy()

    override suspend fun getTotalPriceOfItemsBought(): Flow<Double?> =
        itemDao.getTotalPriceOfItemsBought()

    override suspend fun getCurrentMonthSummaryWithStatusZero(month: String): Flow<List<ItemPriceStatusZero>?> =
        itemDao.getCurrentMonthSummaryWithStatusZero(month).map { entities ->
            entities?.map {
                it.toDomain()
            }
        }

    override suspend fun getCurrentMonthSummaryWithStatusOne(month: String): Flow<List<ItemPriceStatusOne>?> =
        itemDao.getCurrentMonthSummaryWithStatusOne(month).map { entities ->
            entities?.map {
                it.toDomain()
            }
        }

    override suspend fun getMonthlySumForItemsWithStatusZero(): Flow<List<MonthlySumStatusZero>?> =
        itemDao.getMonthlySumForItemsWithStatusZero().map { entities ->
            entities?.map {
                it.toDomain()
            }
        }

    override suspend fun getMonthlySumForItemsWithStatusOne(): Flow<List<MonthlySumStatusOne>?> =
        itemDao.getMonthlySumForItemsWithStatusOne().map { entities ->
            entities?.map {
                it.toDomain()
            }
        }
}