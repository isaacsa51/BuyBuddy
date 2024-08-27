package com.serranoie.android.buybuddy.domain.usecase.item

import android.util.Log
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusOne
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentMonthSummaryItemsBoughtUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(month: String): Flow<UseCaseResult<List<ItemPriceStatusOne>?>> =
        flow {
            try {
                itemRepository.getCurrentMonthSummaryWithStatusOne(month)
                    .collect { data ->
                        emit(UseCaseResult.Success(data))
                    }
            } catch (e: Exception) {
                Log.e("DEBUG", "UseCase Exception: $e")
                emit(UseCaseResult.Error(e))
            }
        }
}