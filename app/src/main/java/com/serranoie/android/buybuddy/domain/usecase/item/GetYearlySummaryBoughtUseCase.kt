package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.MonthlySum
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetYearlySummaryBoughtUseCase @Inject constructor(private val itemRepositoryImpl: ItemRepositoryImpl) {
    suspend fun invoke(): Flow<UseCaseResult<List<MonthlySum>>> = flow {
        try {
            val data = itemRepositoryImpl.getMonthlySumForItemsWithStatusOne().first()
            emit(UseCaseResult.Success(data))
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}