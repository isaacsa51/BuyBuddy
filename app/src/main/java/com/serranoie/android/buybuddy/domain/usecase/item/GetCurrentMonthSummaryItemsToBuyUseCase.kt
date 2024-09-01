package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusZero
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentMonthSummaryItemsToBuyUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(month: String): Flow<UseCaseResult<List<ItemPriceStatusZero>?>> =
        flow {
            try {
                itemRepository.getCurrentMonthSummaryWithStatusZero(month)
                    .collect { data ->
                        emit(UseCaseResult.Success(data))
                    }
            } catch (e: Exception) {
                emit(UseCaseResult.Error(e))
            }
        }
}
