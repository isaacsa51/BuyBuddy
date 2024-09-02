package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Item
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.Date

class InsertItemUseCaseTest {

    private lateinit var itemRepository: ItemRepositoryImpl
    private lateinit var insertItemUseCase: InsertItemUseCase

    @Before
    fun setUp() {
        itemRepository = mockk()
        insertItemUseCase = InsertItemUseCase(itemRepository)
    }

    @Test
    fun `invoke calls insertItem in repository`() = runBlocking {
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

        val insertedItemId = 1L

        coEvery { itemRepository.insertItem(item) } returns insertedItemId

        insertItemUseCase(item)

        coVerify { itemRepository.insertItem(item) }
    }
}