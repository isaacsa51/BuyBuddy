package com.serranoie.android.buybuddy.domain.usecase.category

import app.cash.turbine.test
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.domain.repository.CategoryRepository
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetCategoryWithItemsUseCaseTest {

    private lateinit var categoryRepository: CategoryRepository
    private lateinit var useCase: GetCategoriesWithItemsUseCase

    @Before
    fun setUp() {
        categoryRepository = mockk()
        useCase = GetCategoriesWithItemsUseCase(categoryRepository)
    }

    @Test
    fun `invoke emits Success when repository returns data`() = runBlocking {
        val data = listOf(
            CategoryWithItemsEntity(
                category = CategoryEntity(
                    categoryId = 1,
                    name = "Test Category",
                ),
                items = listOf(
                    ItemEntity(
                        itemId = 1,
                        name = "Test Item",
                        categoryId = 1,
                        description = "Test Description",
                        usage = "Test Usage",
                        benefits = "Test Benefits",
                        disadvantages = "Test Disadvantages",
                        price = 100.0,
                        reminderDate = null,
                        reminderTime = null,
                        status = true
                    )
                )
            )
        )

        coEvery { categoryRepository.getCategoriesWithItems() } returns flowOf(data)

        useCase().test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Success)
            assertEquals(data, (result as UseCaseResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Error when repository throws exception`() = runBlocking {
        val exception = RuntimeException("An error occurred")

        coEvery { categoryRepository.getCategoriesWithItems() } throws exception

        useCase().test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Error)
            assertEquals(exception.message, (result as UseCaseResult.Error).exception.message)
            awaitComplete()
        }
    }
}