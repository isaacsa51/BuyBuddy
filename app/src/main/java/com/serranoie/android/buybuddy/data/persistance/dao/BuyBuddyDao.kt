package com.serranoie.android.buybuddy.data.persistance.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemPriceEntityStatusOne
import com.serranoie.android.buybuddy.data.persistance.entity.ItemPriceEntityStatusZero
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumCategoryEntityStatusOne
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumCategoryEntityStatusZero
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumEntityStatusOne
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumEntityStatusZero
import com.serranoie.android.buybuddy.domain.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyBuddyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity): Long

    @Transaction
    suspend fun insertItemWithCategory(item: ItemEntity, categoryName: String) {
        val existingCategory = getCategoryByName(categoryName)
        val categoryId = existingCategory?.categoryId ?: insertCategory(
            CategoryEntity(
                null, categoryName
            )
        ).toInt()
        val itemWithCategory = ItemEntity(
            itemId = item.itemId,
            name = item.name,
            categoryId = categoryId,
            description = item.description,
            usage = item.usage,
            benefits = item.benefits,
            disadvantages = item.disadvantages,
            price = item.price,
            reminderDate = item.reminderDate,
            reminderTime = item.reminderTime,
            status = item.status
        )
        insertItem(itemWithCategory)
    }

    @Transaction
    suspend fun insertItemWithCategoryAndReturnId(item: ItemEntity, categoryName: String): Long {
        val existingCategory = getCategoryByName(categoryName)
        val categoryId = existingCategory?.categoryId ?: insertCategory(
            CategoryEntity(
                null, categoryName
            )
        ).toInt()
        val itemWithCategory = ItemEntity(
            itemId = item.itemId,
            name = item.name,
            categoryId = categoryId,
            description = item.description,
            usage = item.usage,
            benefits = item.benefits,
            disadvantages = item.disadvantages,
            price = item.price,
            reminderDate = item.reminderDate,
            reminderTime = item.reminderTime,
            status = item.status
        )
        val insertedItemId = insertItem(itemWithCategory)

        return insertedItemId
    }

    @Update
    suspend fun updateItem(item: ItemEntity)

    @Query("UPDATE item SET status = :status WHERE itemId = :itemId")
    suspend fun updateItemStatus(itemId: Int, status: Boolean)

    @Query("DELETE FROM item WHERE itemId = :id")
    suspend fun deleteItem(id: Int)

    @Query("SELECT * FROM item")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM item WHERE itemId = :itemId")
    fun getItemById(itemId: Int): Flow<ItemEntity?>

    @Query("SELECT * FROM item WHERE categoryId = :categoryId")
    fun getItemsForCategory(categoryId: Int): Flow<List<ItemEntity>>

    @Query("SELECT SUM(price) FROM item WHERE status = 0")
    fun getTotalPriceOfItemsToBuy(): Flow<Double?>

    @Query("SELECT * FROM item WHERE strftime('%m', reminderDate / 1000, 'unixepoch') = :currentMonth AND status = 0")
    fun getCurrentMonthSummaryWithStatusZero(currentMonth: String): Flow<List<ItemPriceEntityStatusZero>?>

    @Query("SELECT * FROM item WHERE strftime('%m', reminderDate / 1000, 'unixepoch') = :currentMonth AND status = 1")
    fun getCurrentMonthSummaryWithStatusOne(currentMonth: String): Flow<List<ItemPriceEntityStatusOne>?>

    @Query("SELECT strftime('%Y-%m', reminderDate / 1000, 'unixepoch') AS month, SUM(price) AS totalSum FROM item WHERE status = 0 GROUP BY month")
    fun getMonthlySumForItemsWithStatusZero(): Flow<List<MonthlySumEntityStatusZero>?>

    @Query("SELECT strftime('%Y-%m', reminderDate / 1000, 'unixepoch') AS month, SUM(price) AS totalSum FROM item WHERE status = 1 GROUP BY month")
    fun getMonthlySumForItemsWithStatusOne(): Flow<List<MonthlySumEntityStatusOne>?>

    @Query("SELECT c.name AS categoryName, i.price AS totalPrice FROM item i JOIN category c ON i.categoryId = c.categoryId WHERE i.status = 0 AND strftime('%m', i.reminderDate / 1000, 'unixepoch') = :currentMonth")
    fun getSummaryOfCategoryWithStatusZero(currentMonth: String): Flow<List<MonthlySumCategoryEntityStatusZero>?>

    @Query("SELECT c.name AS categoryName, i.price AS totalPrice FROM item i JOIN category c ON i.categoryId = c.categoryId WHERE i.status = 1 AND strftime('%m', i.reminderDate / 1000, 'unixepoch') = :currentMonth")
    fun getSummaryOfCategoryWithStatusOne(currentMonth: String): Flow<List<MonthlySumCategoryEntityStatusOne>?>

    @Query("SELECT SUM(price) FROM item WHERE status = 1")
    fun getTotalPriceOfItemsBought(): Flow<Double?>

    @Query("SELECT * FROM category WHERE name = :categoryName LIMIT 1")
    suspend fun getCategoryByName(categoryName: String): CategoryEntity?

    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
    fun getCategoryById(categoryId: Int): Flow<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Transaction
    @Query("SELECT * FROM category")
    fun getCategoriesWithItems(): Flow<List<CategoryWithItemsEntity>>
}