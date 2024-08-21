package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusOne
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetYearlySummaryBoughtUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(): Flow<UseCaseResult<List<MonthlySumStatusOne>?>> = flow {
        try {
            itemRepository.getMonthlySumForItemsWithStatusOne().collect { data ->
                emit(UseCaseResult.Success(data))
            }
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}