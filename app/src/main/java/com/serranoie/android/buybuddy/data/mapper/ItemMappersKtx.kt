package com.serranoie.android.buybuddy.data.mapper

import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemPrice
import com.serranoie.android.buybuddy.domain.model.Item

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

fun ItemEntity.toItemPrice(): ItemPrice {
    return ItemPrice(price = this.price)
}