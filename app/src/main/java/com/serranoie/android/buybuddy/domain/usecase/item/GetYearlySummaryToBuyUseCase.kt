package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusZero
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetYearlySummaryToBuyUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(): Flow<UseCaseResult<List<MonthlySumStatusZero>?>> = flow {
        try {
            itemRepository.getMonthlySumForItemsWithStatusZero()
                .collect { data ->
                    emit(UseCaseResult.Success(data))
                }
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}