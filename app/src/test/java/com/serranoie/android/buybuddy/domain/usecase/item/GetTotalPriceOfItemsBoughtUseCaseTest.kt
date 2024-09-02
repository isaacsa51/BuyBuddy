package com.serranoie.android.buybuddy.domain.usecase.item

import app.cash.turbine.test
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetTotalPriceOfItemsBoughtUseCaseTest {

    private lateinit var repository: ItemRepositoryImpl
    private lateinit var useCase: GetTotalPriceOfItemsBoughtUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetTotalPriceOfItemsBoughtUseCase(repository)
    }

    @Test
    fun `invoke emits Success when repository returns data`() = runBlocking {
        val totalPrice = 100.0

        coEvery { repository.getTotalPriceOfItemsBought() } returns flowOf(totalPrice)

        useCase().test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Success)
            assertEquals(totalPrice, (result as UseCaseResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Error when repository throws exception`() = runBlocking {
        val exception = RuntimeException("Total price is null")

        coEvery { repository.getTotalPriceOfItemsBought() } throws exception

        useCase().test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Error)
            assertEquals(exception.message, (result as UseCaseResult.Error).exception.message)
            awaitComplete()
        }
    }
}