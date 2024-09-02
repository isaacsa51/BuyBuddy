package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertItemWithCategoryUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(item: Item, categoryName: String): Flow<UseCaseResult<Int>> = flow {
        try {
            val itemId = itemRepository.insertItemAndReturnId(item, categoryName)
            emit(UseCaseResult.Success(itemId))
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}