package com.serranoie.android.buybuddy.domain.usecase.item

import app.cash.turbine.test
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.Date

class InsertItemWithCategoryUseCaseTest {

    private lateinit var itemRepository: ItemRepositoryImpl
    private lateinit var useCase: InsertItemWithCategoryUseCase

    @Before
    fun setUp() {
        itemRepository = mockk()
        useCase = InsertItemWithCategoryUseCase(itemRepository)
    }

    @Test
    fun `invoke emits Success when repository returns itemId`() = runBlocking {
        val item = Item(
            itemId = 1,
            name = "Test Item",
            categoryId = 123,
            description = "Test Description",
            usage = "Test Usage",
            benefits = "Test Benefits",
            disadvantages = "Test Disadvantages",
            price = 100.0,
            reminderDate = Date(),
            reminderTime = Date(),
            status = true
        )
        val categoryName = "Test Category"
        val itemId = 1

        coEvery { itemRepository.insertItemAndReturnId(item, categoryName) } returns itemId

        useCase(item, categoryName).test {
            val result = awaitItem()
            assertTrue(true)
            assertEquals(itemId, (result as UseCaseResult.Success<*>).data)
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Error when repository throws exception`() = runBlocking {
        val item = Item(
            itemId = 1,
            name = "Test Item",
            categoryId = 123,
            description = "Test Description",
            usage = "Test Usage",
            benefits = "Test Benefits",
            disadvantages = "Test Disadvantages",
            price = 100.0,
            reminderDate = Date(),
            reminderTime = Date(),
            status = true
        )
        val categoryName = "Test Category"
        val exception = RuntimeException("Insert failed")

        coEvery { itemRepository.insertItemAndReturnId(item, categoryName) } throws exception

        useCase(item, categoryName).test {
            val result = awaitItem()
            assertTrue(true)
            assertEquals(exception.message, (result as UseCaseResult.Error).exception.message)
            awaitComplete()
        }
    }
}
