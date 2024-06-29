package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(private val repository: ItemRepositoryImpl) {

    suspend fun deleteItem(itemId: Int) = repository.deleteItem(itemId)
}