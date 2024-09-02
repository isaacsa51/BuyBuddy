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

class GetItemsUseCaseTest {

    private lateinit var itemRepository: ItemRepositoryImpl
    private lateinit var useCase: GetItemsUseCase

    @Before
    fun setUp() {
        itemRepository = mockk()
        useCase = GetItemsUseCase(itemRepository)
    }

    @Test
    fun `invoke return items`() = runBlocking {
        val items = listOf(
            Item(
                itemId = 1,
                name = "Item 1",
                categoryId = 1,
                description = "Description 1",
                usage = "Usage 1",
                benefits = "Benefits 1",
                disadvantages = "Disadvantages 1",
                price = 10.0,
                reminderDate = Date(),
                reminderTime = Date(),
                status = true
            )
        )

        coEvery { itemRepository.getAllItems() } returns flowOf(items)

        useCase().test {
            val result = awaitItem()
            assertEquals(items, result)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns empty list when no items found`() = runBlocking {
        coEvery { itemRepository.getAllItems() } returns flowOf(emptyList())

        useCase().test {
            val result = awaitItem()
            assertEquals(emptyList<Item>(), result)
            awaitComplete()
        }
    }
}