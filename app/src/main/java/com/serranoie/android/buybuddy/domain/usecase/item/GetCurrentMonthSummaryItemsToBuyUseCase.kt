package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.persistance.entity.ItemPriceEntity
import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.ItemPrice
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentMonthSummaryItemsToBuyUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(month: String): Flow<UseCaseResult<List<ItemPrice>>> = flow {
        try {
            val data = itemRepository.getCurrentMonthSummaryWithStatusZero(month).first()
            emit(UseCaseResult.Success(data))
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}
