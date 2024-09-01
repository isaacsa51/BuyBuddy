package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(itemId: Int): UseCaseResult<Unit> {
        return try {
            itemRepository.deleteItem(itemId)
            UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}