package com.serranoie.android.buybuddy.ui.home

import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoriesWithItemsUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetTotalPriceOfItemsBoughtUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetTotalPriceOfItemsToBuyUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.launch
import org.mockito.Mock

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @MockK
    private lateinit var getCategoriesWithItemsUseCase: GetCategoriesWithItemsUseCase

    @MockK
    private lateinit var getTotalPriceOfItemsToBuyUseCase: GetTotalPriceOfItemsToBuyUseCase

    @MockK
    private lateinit var getTotalPriceOfItemsBoughtUseCase: GetTotalPriceOfItemsBoughtUseCase

    @Mock
    private lateinit var userEventsTracker: UserEventsTracker

    private lateinit var viewModel: HomeViewModel

    private val categoryEntityMock = mockk<CategoryEntity>()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = HomeViewModel(
            getCategoriesWithItemsUseCase,
            getTotalPriceOfItemsToBuyUseCase,
            getTotalPriceOfItemsBoughtUseCase,
            userEventsTracker
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test fetchGetCategoriesWithItems success`(): Unit = runTest {
        val categoriesWithItems = listOf(
            CategoryWithItemsEntity(
                category = categoryEntityMock,
                items = emptyList()
            )
        )
        coEvery { getCategoriesWithItemsUseCase.invoke() } returns flowOf(UseCaseResult.Success(categoriesWithItems))

        // When
        viewModel.fetchGetCategoriesWithItems()

        // Then
        val job = launch {
            viewModel.categoriesWithItems.collect { result ->
                assertEquals(categoriesWithItems, result)
            }
        }
        job.cancel()
    }

    @Test
    fun `test fetchTotalPrices success`() = runTest {
        // Given
        val totalPrice = 100.0
        val totalBoughtPrice = 50.0
        coEvery { getTotalPriceOfItemsToBuyUseCase() } returns flowOf(UseCaseResult.Success(totalPrice))
        coEvery { getTotalPriceOfItemsBoughtUseCase() } returns flowOf(UseCaseResult.Success(totalBoughtPrice))

        // When
        viewModel.fetchTotalPrices()

        // Then
        val totalPriceJob = launch {
            viewModel.totalPrice.collect { result ->
                assertEquals(totalPrice, result)
            }
        }
        val totalBoughtPriceJob = launch {
            viewModel.totalBoughtPrice.collect { result ->
                assertEquals(totalBoughtPrice, result)
            }
        }

        totalPriceJob.cancel()
        totalBoughtPriceJob.cancel()
    }

    @Test
    fun `test triggerDataFetch triggers data fetching`() = runTest {
        // Given
        val categoriesWithItems = listOf(
            CategoryWithItemsEntity(
                category = categoryEntityMock,
                items = emptyList()
            )
        )
        coEvery { getCategoriesWithItemsUseCase.invoke() } returns flowOf(UseCaseResult.Success(categoriesWithItems))
        coEvery { getTotalPriceOfItemsToBuyUseCase() } returns flowOf(UseCaseResult.Success(100.0))
        coEvery { getTotalPriceOfItemsBoughtUseCase() } returns flowOf(UseCaseResult.Success(50.0))

        // When
        viewModel.triggerDataFetch()

        // Then
        val categoriesJob = launch {
            viewModel.categoriesWithItems.collect { result ->
                assertEquals(categoriesWithItems, result)
            }
        }
        val totalPriceJob = launch {
            viewModel.totalPrice.collect { result ->
                assertEquals(100.0, result)
            }
        }
        val totalBoughtPriceJob = launch {
            viewModel.totalBoughtPrice.collect { result ->
                assertEquals(50.0, result)
            }
        }

        categoriesJob.cancel()
        totalPriceJob.cancel()
        totalBoughtPriceJob.cancel()
    }
}