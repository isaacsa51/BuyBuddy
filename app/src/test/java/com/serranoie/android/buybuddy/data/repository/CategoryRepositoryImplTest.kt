package com.serranoie.android.buybuddy.data.repository

import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumCategoryEntityStatusOne
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumCategoryEntityStatusZero
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.Date

class CategoryRepositoryImplTest {
    @MockK
    private lateinit var categoryDao: BuyBuddyDao

    private lateinit var categoryRepository: CategoryRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        categoryRepository = CategoryRepositoryImpl(categoryDao)
    }

    @Test
    fun `get categories with items`() = runBlocking {
        val categoryWithItemsEntities = listOf(
            CategoryWithItemsEntity(
                category = CategoryEntity(1, "Category1"),
                items = listOf(
                    ItemEntity(
                        1,
                        "Item1",
                        1,
                        "desc",
                        "usage",
                        "benefits",
                        "disadvantages",
                        10.0,
                        Date(),
                        Date(),
                        true
                    )
                )
            )
        )

        coEvery { categoryDao.getCategoriesWithItems() } returns flowOf(categoryWithItemsEntities)

        val result = categoryRepository.getCategoriesWithItems()

        coVerify { categoryDao.getCategoriesWithItems() }
        result.collect { categoriesWithItems ->
            assertEquals(1, categoriesWithItems.size)
            assertEquals("Category1", categoriesWithItems[0].category.name)
        }
    }

    @Test
    fun `test getCategoryById`() = runBlocking {
        val categoryId = 1
        val categoryEntity = CategoryEntity(1, "Category1")

        coEvery { categoryDao.getCategoryById(categoryId) } returns flowOf(categoryEntity)

        val result = categoryRepository.getCategoryById(categoryId)

        coVerify { categoryDao.getCategoryById(categoryId) }
        result.collect { category ->
            assertEquals("Category1", category.name)
        }
    }

    @Test
    fun `test getCategorySummaryWithStatusZero`() = runBlocking {
        val month = "2024-09"
        val monthlySumEntities = listOf(
            MonthlySumCategoryEntityStatusZero("2024-09", listOf(2000.0))
        )

        coEvery { categoryDao.getSummaryOfCategoryWithStatusZero(month) } returns flowOf(
            monthlySumEntities
        )

        val result = categoryRepository.getCategorySummaryWithStatusZero(month)

        coVerify { categoryDao.getSummaryOfCategoryWithStatusZero(month) }
        result.collect { summaries ->
            assertEquals(1, summaries?.size)
            assertEquals(month, summaries?.get(0)?.categoryName)
            assertEquals(listOf(2000.0), summaries?.get(0)?.totalPrice)
        }
    }

    @Test
    fun `test getCategorySummaryWithStatusOne`() = runBlocking {
        val month = "2024-09"
        val monthlySumCategoryEntities = listOf(
            MonthlySumCategoryEntityStatusOne(
                "2024-09", listOf(3000.0)
            )
        )

        coEvery { categoryDao.getSummaryOfCategoryWithStatusOne(month) } returns flowOf(
            monthlySumCategoryEntities
        )

        val result = categoryRepository.getCategorySummaryWithStatusOne(month)

        coVerify { categoryDao.getSummaryOfCategoryWithStatusOne(month) }
        result.collect { summaries ->
            assertEquals(1, summaries?.size)
            assertEquals(month, summaries?.get(0)?.categoryName)
            assertEquals(listOf(3000.0), summaries?.get(0)?.totalPrice)
        }
    }
}