package com.serranoie.android.buybuddy.ui.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoriesWithItemsUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetTotalPriceOfItemsBoughtUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetTotalPriceOfItemsToBuyUseCase
import com.serranoie.android.buybuddy.ui.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: HomeViewModel

    private val getCategoriesWithItemsUseCase: GetCategoriesWithItemsUseCase = mockk()
    private val getTotalPriceOfItemsToBuyUseCase: GetTotalPriceOfItemsToBuyUseCase = mockk()
    private val getTotalPriceOfItemsBoughtUseCase: GetTotalPriceOfItemsBoughtUseCase = mockk()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getCategoriesWithItemsUseCase() } returns flowOf(emptyList())
        coEvery { getTotalPriceOfItemsToBuyUseCase() } returns flowOf(100.0)
        coEvery { getTotalPriceOfItemsBoughtUseCase() } returns flowOf(50.0)

        viewModel =
            HomeViewModel(
                getCategoriesWithItemsUseCase,
                getTotalPriceOfItemsToBuyUseCase,
                getTotalPriceOfItemsBoughtUseCase,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

//    @Test
//    fun testInitialValues() {
//        Assert.assertTrue(viewModel.categoriesWithItems.value.isEmpty())
//        Assert.assertTrue(viewModel.isLoading.value)
//        Assert.assertEquals(0.0, viewModel.totalPrice.value, 0.0)
//        Assert.assertEquals(0.0, viewModel.totalBoughtPrice.value, 0.0)
//    }
//
//    @Test
//    fun testFetchTotalPrices() = runTest {
//        val expectedTotalPrice = 100.0
//        val expectedTotalBoughtPrice = 50.0
//        coEvery { getTotalPriceOfItemsToBuyUseCase() } returns flowOf(expectedTotalPrice)
//        coEvery { getTotalPriceOfItemsBoughtUseCase() } returns flowOf(expectedTotalBoughtPrice)
//
//        viewModel.fetchTotalPrices()
//
//        advanceUntilIdle()
//
//        Assert.assertEquals(expectedTotalPrice, viewModel.totalPrice.value, 0.0)
//        Assert.assertEquals(expectedTotalBoughtPrice, viewModel.totalBoughtPrice.value, 0.0)
//    }
//
//    @Test
//    fun testFetchGetCategoriesWithItems() = runTest {
//        val mockCategoryEntity = mockk<CategoryEntity>()
//        val mockItemEntity = mockk<ItemEntity>()
//
//        val mockCategoryWithItemsEntity = CategoryWithItemsEntity(
//            category = mockCategoryEntity,
//            items = listOf(mockItemEntity)
//        )
//
//        val expectedList = listOf(mockCategoryWithItemsEntity)
//
//        coEvery { getCategoriesWithItemsUseCase.invoke() } returns flowOf(expectedList)
//
//        viewModel.fetchGetCategoriesWithItems()
//        advanceUntilIdle()
//
//        Assert.assertEquals(expectedList, viewModel.categoriesWithItems.value)
//        Assert.assertFalse(viewModel.isLoading.value)
//    }
//
//    @Test
//    fun testFetchGetCategoriesWithItems_empty() = runTest {
//        val mockCategoryEntity = mockk<CategoryEntity>()
//        val mockCategoryWithItemsEntity = CategoryWithItemsEntity(
//            category = mockCategoryEntity,
//            items = emptyList()
//        )
//
//        val expectedList = emptyList<CategoryWithItemsEntity>()
//
//        coEvery { getCategoriesWithItemsUseCase.invoke() } returns flowOf(expectedList)
//        advanceUntilIdle()
//
//        Assert.assertEquals(expectedList, viewModel.categoriesWithItems.value)
//        Assert.assertFalse(viewModel.isLoading.value)
//    }
}
