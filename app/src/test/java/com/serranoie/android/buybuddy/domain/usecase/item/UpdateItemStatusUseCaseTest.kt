package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateItemStatusUseCaseTest {

    private lateinit var repository: ItemRepositoryImpl
    private lateinit var useCase: UpdateItemStatusUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = UpdateItemStatusUseCase(repository)
    }

    @Test
    fun `invoke calls updateItemStatus in repository`() = runBlocking {
        val itemId = 1
        val status = true

        coEvery { repository.updateItemStatus(itemId, status) } returns Unit

        useCase(itemId, status)

        coVerify { repository.updateItemStatus(itemId, status) }
    }
}