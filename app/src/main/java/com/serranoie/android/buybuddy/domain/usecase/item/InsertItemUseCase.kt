package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.model.Item
import javax.inject.Inject

class InsertItemUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(item: Item) = itemRepository.insertItem(item)
}