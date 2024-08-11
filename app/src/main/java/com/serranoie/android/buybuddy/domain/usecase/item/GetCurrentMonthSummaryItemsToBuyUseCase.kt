package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import javax.inject.Inject

class GetCurrentMonthSummaryItemsToBuyUseCase @Inject constructor(private val itemRepository: ItemRepositoryImpl) {
    suspend operator fun invoke(month: String) = itemRepository.getSummaryToBuyPricesByMonth(month)
}
