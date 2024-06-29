package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import javax.inject.Inject

class UpdateItemStatusUseCase @Inject constructor(private val repository: ItemRepositoryImpl) {
    suspend operator fun invoke(id: Int, status: Boolean) {
        repository.updateItemStatus(id, status)
    }
}