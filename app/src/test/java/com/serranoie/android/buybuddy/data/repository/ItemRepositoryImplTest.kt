package com.serranoie.android.buybuddy.data.repository

import com.serranoie.android.buybuddy.data.mapper.toDomain
import com.serranoie.android.buybuddy.data.mapper.toEntity
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.domain.model.Item
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.Date

class ItemRepositoryImplTest {

    @MockK
    private lateinit var itemDao: BuyBuddyDao

    private lateinit var itemRepository: ItemRepositoryImpl

    private val item = Item(
        itemId = 1,
        name = "Item",
        categoryId = 1,
        description = "Description",
        usage = "Usage",
        benefits = "Benefits",
        disadvantages = "Disadvantages",
        price = 100.0,
        reminderTime = Date(),
        reminderDate = Date(),
        status = true
    )
    private val categoryName = "Category"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        itemRepository = ItemRepositoryImpl(itemDao)
    }

    @Test
    fun `insertItemWithCategory calls dao correctly`() = runTest {
        coEvery { itemDao.insertItemWithCategory(any(), any()) } just Runs

        itemRepository.insertItemWithCategory(item, categoryName)

        coVerify(exactly = 1) { itemDao.insertItemWithCategory(item.toEntity(0), categoryName) }
    }

    @Test
    fun `insertItemAndReturnId calls dao correctly`() = runTest {
        coEvery { itemDao.insertItemWithCategoryAndReturnId(any(), categoryName) } returns 1L

        val result = itemRepository.insertItemAndReturnId(item, categoryName)

        assertEquals(1, result)
        coVerify { itemDao.insertItemWithCategoryAndReturnId(item.toEntity(0), categoryName) }
    }

    @Test
    fun `test insertItem`() = runBlocking {
        val slotItemEntity = slot<ItemEntity>()

        coEvery { itemDao.insertItem(capture(slotItemEntity)) } returns 1L

        itemRepository.insertItem(item)

        coVerify { itemDao.insertItem(any()) }
        assertEquals(item.itemId, slotItemEntity.captured.itemId)
        assertEquals(item.categoryId, slotItemEntity.captured.categoryId)
        assertEquals(item.name, slotItemEntity.captured.name)
    }

    @Test
    fun `getAllItems from dao`() = runTest {
        val itemEntity = ItemEntity(
            itemId = 1,
            name = "Item",
            categoryId = 1,
            description = "Description",
            usage = "Usage",
            benefits = "Benefits",
            disadvantages = "Disadvantages",
            price = 100.0,
            reminderTime = Date(),
            reminderDate = Date(),
            status = true
        )
        val itemEntities = listOf(itemEntity)
        val expectedItems = listOf(itemEntity.toDomain())

        coEvery { itemDao.getAllItems() } returns flowOf(itemEntities)

        val result = itemRepository.getAllItems().first()

        assertEquals(expectedItems, result)
    }

    @Test
    fun `test updateItem`() = runBlocking {
        val slotItemEntity = slot<ItemEntity>()

        coEvery { itemDao.updateItem(capture(slotItemEntity)) } returns Unit

        itemRepository.updateItem(item)

        coVerify { itemDao.updateItem(any()) }
        assertEquals(item.itemId, slotItemEntity.captured.itemId)
        assertEquals(item.categoryId, slotItemEntity.captured.categoryId)
        assertEquals(item.name, slotItemEntity.captured.name)
    }

    @Test
    fun `test updateItemStatus`() = runBlocking {
        val itemId = 1
        val status = true

        coEvery { itemDao.updateItemStatus(itemId, status) } returns Unit

        itemRepository.updateItemStatus(itemId, status)

        coVerify { itemDao.updateItemStatus(itemId, status) }
    }

    @Test
    fun `test deleteItem`() = runBlocking {
        val itemId = 1

        coEvery { itemDao.deleteItem(itemId) } returns Unit

        itemRepository.deleteItem(itemId)

        coVerify { itemDao.deleteItem(itemId) }
    }
}