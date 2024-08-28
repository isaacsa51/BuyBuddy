package com.serranoie.android.buybuddy.domain.usecase.category

import com.serranoie.android.buybuddy.data.repository.CategoryRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(private val categoryRepository: CategoryRepositoryImpl) {
    suspend fun getCategoryById(id: Int): Flow<UseCaseResult<Category>> = flow {
        try {
            categoryRepository.getCategoryById(id).collect {
                emit(UseCaseResult.Success(it))
            }
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}