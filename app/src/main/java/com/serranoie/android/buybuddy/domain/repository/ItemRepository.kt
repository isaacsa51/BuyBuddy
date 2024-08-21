package com.serranoie.android.buybuddy.domain.repository

import com.serranoie.android.buybuddy.data.persistance.entity.ItemPriceEntity
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumEntity
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.model.ItemPrice
import com.serranoie.android.buybuddy.domain.model.MonthlySum
import kotlinx.coroutines.flow.Flow

interface ItemRepository {

    suspend fun insertItemWithCategory(item: Item, categoryName: String)
    suspend fun insertItem(item: Item)
    suspend fun updateItem(item: Item)
    suspend fun updateItemStatus(itemId: Int, status: Boolean)
    suspend fun deleteItem(itemId: Int)
    fun getAllItems(): Flow<List<Item?>>
    fun getItemById(itemId: Int): Flow<Item?>
    suspend fun getTotalPriceOfItemsToBuy(): Flow<Double?>
    suspend fun getTotalPriceOfItemsBought(): Flow<Double?>
    suspend fun getCurrentMonthSummaryWithStatusZero(month: String): Flow<List<ItemPrice>?>
    suspend fun getCurrentMonthSummaryWithStatusOne(month: String): Flow<List<ItemPrice>?>
    suspend fun getMonthlySumForItemsWithStatusZero(): Flow<List<MonthlySum>?>
    suspend fun getMonthlySumForItemsWithStatusOne(): Flow<List<MonthlySum>?>

}