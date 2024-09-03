package com.serranoie.android.buybuddy.data.mapper

import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemPriceEntityStatusOne
import com.serranoie.android.buybuddy.data.persistance.entity.ItemPriceEntityStatusZero
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumEntityStatusOne
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumEntityStatusZero
import com.serranoie.android.buybuddy.domain.model.Item
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.Date

class ItemMappersTest {

    private val date = Date()

    @Test
    fun `ItemEntity toDomain maps correctly`() {
        val itemEntity = ItemEntity(
            itemId = 1, name = "Test Item", categoryId = 1, description = "Test description",
            usage = "Test usage", benefits = "Test benefits", disadvantages = "Test disadvantages",
            price = 100.0, reminderDate = date, reminderTime = date, status = true
        )

        val item = itemEntity.toDomain()

        assertEquals(itemEntity.itemId, item.itemId)
        assertEquals(itemEntity.name, item.name)
        assertEquals(itemEntity.categoryId, item.categoryId)
        assertEquals(itemEntity.description, item.description)
        assertEquals(itemEntity.price, item.price, 0.0)
        assertEquals(itemEntity.status, item.status)
    }

    @Test
    fun `Item toEntity maps correctly`() {
        val item = Item(
            itemId = 1, name = "Test Item", categoryId = 1, description = "Test description",
            usage = "Test usage", benefits = "Test benefits", disadvantages = "Test disadvantages",
            price = 100.0, reminderDate = date, reminderTime = date, status = true
        )

        val itemEntity = item.toEntity(2)

        assertEquals(item.itemId, itemEntity.itemId)
        assertEquals(item.name, itemEntity.name)
        assertEquals(2, itemEntity.categoryId)
        assertEquals(item.description, itemEntity.description)
        assertEquals(item.price, itemEntity.price, 0.0)
        assertEquals(item.status, itemEntity.status)
    }

    @Test
    fun `ItemPriceEntityStatusZero toDomain maps correctly`() {
        val itemPriceEntity = ItemPriceEntityStatusZero(price = 200.0)
        val domain = itemPriceEntity.toDomain()

        assertEquals(itemPriceEntity.price!!, domain.price!!, 0.0)
    }

    @Test
    fun `ItemPriceEntityStatusOne toDomain maps correctly`() {
        val itemPriceEntity = ItemPriceEntityStatusOne(price = 150.0)
        val domain = itemPriceEntity.toDomain()

        assertEquals(itemPriceEntity.price!!, domain.price!!, 0.0)
    }

    @Test
    fun `MonthlySumEntityStatusZero toDomain maps correctly`() {
        val monthlySumEntity = MonthlySumEntityStatusZero(month = "August", totalSum = 1000.0)
        val domain = monthlySumEntity.toDomain()

        assertEquals(monthlySumEntity.month, domain.month)
        assertEquals(monthlySumEntity.totalSum!!, domain.totalSum!!, 0.0)
    }

    @Test
    fun `MonthlySumEntityStatusOne toDomain maps correctly`() {
        val monthlySumEntity = MonthlySumEntityStatusOne(month = "July", totalSum = 1500.0)
        val domain = monthlySumEntity.toDomain()

        assertEquals(monthlySumEntity.month, domain.month)
        assertEquals(monthlySumEntity.totalSum!!, domain.totalSum!!, 0.0)
    }
}