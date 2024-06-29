package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Item
import javax.inject.Inject

class UpdateItemUseCase @Inject constructor(private val repository: ItemRepositoryImpl) {
    suspend operator fun invoke(item: Item) = repository.updateItem(item)
}