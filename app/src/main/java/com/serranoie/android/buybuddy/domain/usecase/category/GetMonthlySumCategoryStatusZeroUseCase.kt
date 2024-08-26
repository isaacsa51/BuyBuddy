package com.serranoie.android.buybuddy.domain.usecase.category

import com.serranoie.android.buybuddy.data.repository.CategoryRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusZero
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMonthlySumCategoryStatusZeroUseCase @Inject constructor(private val categoryRepository: CategoryRepositoryImpl) {

    suspend operator fun invoke(month: String): Flow<UseCaseResult<List<MonthlySumCategoryStatusZero>?>> = flow {
        try {
            categoryRepository.getCategorySummaryWithStatusZero(month).collect {
                emit(UseCaseResult.Success(it))
            }
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }

}