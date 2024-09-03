package com.serranoie.android.buybuddy.domain.usecase.item

import app.cash.turbine.test
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteItemUseCaseTest {

    private lateinit var repository: ItemRepositoryImpl
    private lateinit var useCase: DeleteItemUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = DeleteItemUseCase(repository)
    }

    @Test
    fun `invoke emits Success when deleting item`() = runTest {
        val itemId = 1

        coEvery { repository.deleteItem(itemId) } returns Unit

        val resultFlow = flow { emit(useCase(itemId)) }

        resultFlow.test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Success)
            awaitComplete()
        }

        coVerify { repository.deleteItem(itemId) }
    }

    @Test
    fun `invoke emits Error when deleting item`() = runTest {
        val itemId = 1
        val exception = RuntimeException("Something went wrong")

        coEvery { repository.deleteItem(itemId) } throws exception

        val resultFlow = flow { emit(useCase(itemId)) }

        resultFlow.test {
            val result = awaitItem()
            assertTrue(result is UseCaseResult.Error)
            awaitComplete()
        }

        coVerify { repository.deleteItem(itemId) }
    }
}