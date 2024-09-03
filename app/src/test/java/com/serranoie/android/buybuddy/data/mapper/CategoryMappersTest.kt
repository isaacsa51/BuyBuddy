package com.serranoie.android.buybuddy.data.mapper

import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumCategoryEntityStatusOne
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumCategoryEntityStatusZero
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.model.CategoryWithItems
import com.serranoie.android.buybuddy.domain.model.Item
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.Date

class CategoryMappersTest {

    private val date = Date()

    @Test
    fun `CategoryEntity toDomain maps correctly`() {
        val categoryEntity = CategoryEntity(
            categoryId = 1, name = "Test Category"
        )

        val category = categoryEntity.toDomain()

        assertEquals(categoryEntity.categoryId, category.categoryId)
        assertEquals(categoryEntity.name, category.name)
    }

    @Test
    fun `Category toEntity maps correctly`() {
        val category = Category(
            categoryId = 1, name = "Test Category"
        )

        val categoryEntity = category.toEntity()

        assertEquals(category.categoryId, categoryEntity.categoryId)
        assertEquals(category.name, categoryEntity.name)
    }

    @Test
    fun `CategoryWithItemsEntity toDomain maps correctly`() {
        val categoryWithItemEntity = CategoryWithItemsEntity(
            category = CategoryEntity(categoryId = 1, name = "Test Category"),
            items = listOf(
                ItemEntity(
                    itemId = 1,
                    name = "Test Item",
                    categoryId = 1,
                    description = "Test description",
                    usage = "Test usage",
                    benefits = "Test benefits",
                    disadvantages = "Test disadvantages",
                    price = 100.0,
                    reminderDate = date,
                    reminderTime = date,
                    status = true
                )
            )
        )

        val categoryWithItems = categoryWithItemEntity.toDomain()

        assertEquals(categoryWithItemEntity.items.size, categoryWithItems.items.size)
    }

    @Test
    fun `CategoryWithItems toEntity maps correctly`() {
        val categoryWithItems = CategoryWithItems(
            items = listOf(
                Item(
                    itemId = 1,
                    name = "Test Item",
                    categoryId = 1,
                    description = "Test description",
                    usage = "Test usage",
                    benefits = "Test benefits",
                    disadvantages = "Test disadvantages",
                    price = 100.0,
                    reminderDate = date,
                    reminderTime = date,
                    status = true
                )
            )
        )

        val categoryEntity = CategoryEntity(categoryId = 1, name = "Test Category")
        val categoryWithItemEntity = categoryWithItems.toEntity(categoryEntity)

        assertEquals(categoryWithItems.items.size, categoryWithItemEntity.items.size)
    }

    @Test
    fun `MonthlySumCategoryEntityStatusZero toDomain maps correctly`() {
        val monthlySumCategoryEntity = MonthlySumCategoryEntityStatusZero(
            categoryName = "Test Category",
            totalPrice = listOf(100.0)
        )

        val monthlySumCategory = monthlySumCategoryEntity.toDomain()

        assertEquals(monthlySumCategoryEntity.categoryName, monthlySumCategory.categoryName)
        assertEquals(monthlySumCategoryEntity.totalPrice, monthlySumCategory.totalPrice)
    }

    @Test
    fun `MonthlySumCategoryEntityStatusOne toDomain maps correctly`() {
        val monthlySumCategoryEntity = MonthlySumCategoryEntityStatusOne(
            categoryName = "Test Category",
            totalPrice = listOf(100.0)
        )

        val monthlySumCategory = monthlySumCategoryEntity.toDomain()

        assertEquals(monthlySumCategoryEntity.categoryName, monthlySumCategory.categoryName)
        assertEquals(monthlySumCategoryEntity.totalPrice, monthlySumCategory.totalPrice)
    }
}
