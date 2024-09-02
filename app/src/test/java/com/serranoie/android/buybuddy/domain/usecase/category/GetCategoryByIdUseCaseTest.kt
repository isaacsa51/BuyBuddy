package com.serranoie.android.buybuddy.domain.usecase.category

import app.cash.turbine.test
import com.serranoie.android.buybuddy.data.repository.CategoryRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetCategoryByIdUseCaseTest {

    private lateinit var categoryRepository: CategoryRepositoryImpl
    private lateinit var useCase: GetCategoryByIdUseCase

    @Before
    fun setUp() {
        categoryRepository = mockk()
        useCase = GetCategoryByIdUseCase(categoryRepository)
    }

    @Test
    fun `getCategoryById emits Success when repository returns data`() = runBlocking {
        val category = Category(
            categoryId = 1,
            name = "Test Category",
        )

        coEvery { categoryRepository.getCategoryById(1) } returns flowOf(category)

        useCase.getCategoryById(1).test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Success)
            assertEquals(category, (result as UseCaseResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `getCategoryById emits Error when repository throws exception`() = runBlocking {
        val exception = RuntimeException("Category not found")

        coEvery { categoryRepository.getCategoryById(1) } throws exception

        useCase.getCategoryById(1).test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Error)
            assertEquals(exception.message, (result as UseCaseResult.Error).exception.message)
            awaitComplete()
        }
    }
}