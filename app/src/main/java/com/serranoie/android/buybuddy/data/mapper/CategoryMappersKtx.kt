package com.serranoie.android.buybuddy.data.mapper

import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.model.CategoryWithItems

fun CategoryEntity.toDomain(): Category {
    return Category(
        categoryId = this.categoryId ?: 0,
        name = this.name,
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        categoryId = this.categoryId,
        name = this.name,
    )
}

fun List<CategoryWithItemsEntity>.toDomain(): List<CategoryWithItems> {
    return this.map { it.toDomain() }
}

fun CategoryWithItemsEntity.toDomain(): CategoryWithItems {
    return CategoryWithItems(
        items = this.items.map { it.toDomain() }
    )
}

fun CategoryWithItems.toEntity(categoryEntity: CategoryEntity): CategoryWithItemsEntity {
    return CategoryWithItemsEntity(
        category = categoryEntity,
        items = this.items.mapNotNull { it?.toEntity(categoryEntity.categoryId ?: 0) }
    )
}
