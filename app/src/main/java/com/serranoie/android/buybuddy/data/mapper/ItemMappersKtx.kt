package com.serranoie.android.buybuddy.data.mapper

import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemPriceEntityStatusOne
import com.serranoie.android.buybuddy.data.persistance.entity.ItemPriceEntityStatusZero
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumEntityStatusOne
import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumEntityStatusZero
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusOne
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusZero
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusOne
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusZero

fun ItemEntity.toDomain(): Item {
    return Item(
        itemId = this.itemId,
        name = this.name,
        categoryId = this.categoryId,
        description = this.description,
        usage = this.usage,
        benefits = this.benefits,
        disadvantages = this.disadvantages,
        price = this.price,
        reminderDate = this.reminderDate,
        reminderTime = this.reminderTime,
        status = this.status
    )
}

fun Item.toEntity(categoryId: Int): ItemEntity {
    return ItemEntity(
        itemId = this.itemId,
        name = this.name,
        categoryId = categoryId,
        description = this.description,
        usage = this.usage,
        benefits = this.benefits,
        disadvantages = this.disadvantages,
        price = this.price,
        reminderDate = this.reminderDate,
        reminderTime = this.reminderTime,
        status = this.status
    )
}

fun ItemPriceEntityStatusZero.toDomain(): ItemPriceStatusZero {
    return ItemPriceStatusZero(price = this.price)
}

fun ItemPriceEntityStatusOne.toDomain(): ItemPriceStatusOne {
    return ItemPriceStatusOne(price = this.price)
}

fun MonthlySumEntityStatusZero.toDomain(): MonthlySumStatusZero {
    return MonthlySumStatusZero(month = this.month, totalSum = this.totalSum)
}

fun MonthlySumEntityStatusOne.toDomain(): MonthlySumStatusOne {
    return MonthlySumStatusOne(month = this.month, totalSum = this.totalSum)
}