package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.persistance.entity.MonthlySumEntity
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.MonthlySum
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetYearlySummaryToBuyUseCase @Inject constructor(private val itemRepositoryImpl: ItemRepositoryImpl) {
    suspend operator fun invoke(): Flow<UseCaseResult<List<MonthlySum>>> = flow {
        try {
            val data = itemRepositoryImpl.getMonthlySumForItemsWithStatusZero().first()
            emit(UseCaseResult.Success(data))
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}