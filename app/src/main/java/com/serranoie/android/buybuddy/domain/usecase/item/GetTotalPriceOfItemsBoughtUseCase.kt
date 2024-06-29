package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalPriceOfItemsBoughtUseCase @Inject constructor(private val repository: ItemRepositoryImpl) {
    suspend operator fun invoke(): Flow<Double?> = repository.getTotalPriceOfItemsBought()
}
