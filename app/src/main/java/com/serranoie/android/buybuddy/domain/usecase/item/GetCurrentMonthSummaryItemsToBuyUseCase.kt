package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.persistance.entity.ItemPrice
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentMonthSummaryItemsToBuyUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(month: String): Flow<UseCaseResult<List<ItemPrice>>> = flow {
        try {
            val itemEntities = itemRepository.getCurrentMonthSummaryWithStatusZero(month).first()
            emit(UseCaseResult.Success(itemEntities))
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}
