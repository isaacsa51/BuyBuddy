package com.serranoie.android.buybuddy.domain.usecase.category

import com.serranoie.android.buybuddy.data.repository.CategoryRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(private val categoryRepository: CategoryRepositoryImpl) {
    suspend fun getCategoryById(id: Int): Flow<Category> {
        return categoryRepository.getCategoryById(id)
    }
}