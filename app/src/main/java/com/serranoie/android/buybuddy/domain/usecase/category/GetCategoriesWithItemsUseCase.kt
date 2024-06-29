package com.serranoie.android.buybuddy.domain.usecase.category

import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.domain.model.CategoryWithItems
import com.serranoie.android.buybuddy.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesWithItemsUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<CategoryWithItemsEntity>> {
        return categoryRepository.getCategoriesWithItems()
    }
}
