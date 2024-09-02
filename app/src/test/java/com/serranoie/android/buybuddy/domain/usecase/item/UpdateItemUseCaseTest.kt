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

class UpdateItemUseCaseTest {

    private lateinit var repository: ItemRepositoryImpl
    private lateinit var updateItemUseCase: UpdateItemUseCase

    @Before
    fun setUp() {
        repository = mockk()
        updateItemUseCase = UpdateItemUseCase(repository)
    }

    @Test
    fun `invoke calls updateItem in repository`() = runBlocking {
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

        coEvery { repository.updateItem(item) } returns Unit

        updateItemUseCase(item)

        coVerify { repository.updateItem(item) }
    }
}