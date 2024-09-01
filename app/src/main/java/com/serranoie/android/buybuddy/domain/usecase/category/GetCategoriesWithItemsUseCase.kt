package com.serranoie.android.buybuddy.domain.usecase.category

import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.domain.repository.CategoryRepository
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCategoriesWithItemsUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<UseCaseResult<List<CategoryWithItemsEntity>>> = flow {
        try {
            categoryRepository.getCategoriesWithItems().collect {
                emit(UseCaseResult.Success(it))
            }
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}
