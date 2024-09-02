package com.serranoie.android.buybuddy.domain.usecase.item

import app.cash.turbine.test
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusOne
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCurrentMonthSummaryItemsBoughtUseCaseTest {

    private lateinit var repository: ItemRepositoryImpl
    private lateinit var useCase: GetCurrentMonthSummaryItemsBoughtUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetCurrentMonthSummaryItemsBoughtUseCase(repository)
    }

    @Test
    fun `invoke emits Success when repository returns data`() = runTest {
        val month = "2024-09"
        val items = listOf(
            ItemPriceStatusOne(price = 100.0),
            ItemPriceStatusOne(price = 200.0)
        )

        coEvery { repository.getCurrentMonthSummaryWithStatusOne(month) } returns flowOf(items)

        useCase(month).test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Success)
            assertEquals(items, (result as UseCaseResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Error when repository throws exception`() = runTest {
        val month = "2024-09"
        val exception = RuntimeException("Something went wrong")

        coEvery { repository.getCurrentMonthSummaryWithStatusOne(month) } throws exception

        useCase(month).test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Error)
            assertEquals(exception, (result as UseCaseResult.Error).exception)
            awaitComplete()
        }
    }
}