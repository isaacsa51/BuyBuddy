package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    operator fun invoke(id: Int) = itemRepository.getItemById(id)
}