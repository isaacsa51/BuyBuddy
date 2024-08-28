package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import javax.inject.Inject

class InsertItemWithCategoryUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(item: Item, categoryName: String): UseCaseResult<Int> {
        return try {
            val itemId = itemRepository.insertItemAndReturnId(item, categoryName)
            UseCaseResult.Success(itemId)
        } catch (e: Exception) {
            UseCaseResult.Error(e)
        }
    }
}