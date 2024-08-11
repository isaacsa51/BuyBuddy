package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {

    suspend operator fun invoke(month: String): Flow<UseCaseResult<List<Item>>> = flow {
        try {
            val items = itemRepository.getSummaryToBuyPricesByMonth(month).first()
            emit(UseCaseResult.Success(items))
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}