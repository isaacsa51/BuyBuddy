package com.serranoie.android.buybuddy.domain.usecase.category

import app.cash.turbine.test
import com.serranoie.android.buybuddy.data.repository.CategoryRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusZero
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetMonthlySumCategoryStatusZeroUseCaseTest {

    private lateinit var categoryRepository: CategoryRepositoryImpl
    private lateinit var useCase: GetMonthlySumCategoryStatusZeroUseCase

    @Before
    fun setUp() {
        categoryRepository = mockk()
        useCase = GetMonthlySumCategoryStatusZeroUseCase(categoryRepository)
    }

    @Test
    fun `invoke emits Success when repository returns data`() = runBlocking {
        val data = listOf(
            MonthlySumCategoryStatusZero(
                categoryName = "Category",
                totalPrice = listOf(100.0)
            )
        )

        coEvery { categoryRepository.getCategorySummaryWithStatusZero("September") } returns flowOf(
            data
        )

        useCase("September").test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Success)
            assertEquals(data, (result as UseCaseResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Error when repository throws exception`() = runBlocking {
        val exception = RuntimeException("An error occurred")

        coEvery { categoryRepository.getCategorySummaryWithStatusZero("September") } throws exception

        useCase("September").test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Error)
            assertEquals(exception.message, (result as UseCaseResult.Error).exception.message)
            awaitComplete()
        }
    }
}