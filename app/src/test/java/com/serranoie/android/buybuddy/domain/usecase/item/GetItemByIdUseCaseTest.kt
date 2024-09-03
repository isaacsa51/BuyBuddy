package com.serranoie.android.buybuddy.domain.usecase.item

import app.cash.turbine.test
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Item
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.Date

class GetItemByIdUseCaseTest {

    private lateinit var itemRepository: ItemRepositoryImpl
    private lateinit var getItemByIdUseCase: GetItemByIdUseCase

    @Before
    fun setUp() {
        itemRepository = mockk()
        getItemByIdUseCase = GetItemByIdUseCase(itemRepository)
    }

    @Test
    fun `test invoke with valid id returns item`() = runBlocking {
        val itemId = 1
        val expectedItem = Item(
            itemId = itemId,
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

        coEvery { itemRepository.getItemById(itemId) } returns flowOf(expectedItem)

        getItemByIdUseCase(itemId).test {
            val result = awaitItem()
            assertEquals(expectedItem, result)
            awaitComplete()
        }
    }

    @Test
    fun `test invoke with invalid id returns null`() = runBlocking {
        val itemId = 999

        coEvery { itemRepository.getItemById(itemId) } returns flowOf(null)

        getItemByIdUseCase(itemId).test {
            val result = awaitItem()
            assertEquals(null, result)
            awaitComplete()
        }
    }
}