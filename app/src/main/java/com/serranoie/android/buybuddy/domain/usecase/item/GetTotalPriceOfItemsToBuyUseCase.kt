package com.serranoie.android.buybuddy.domain.usecase.item

import com.serranoie.android.buybuddy.data.repository.ItemRepositoryImpl
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTotalPriceOfItemsToBuyUseCase @Inject constructor(private val repository: ItemRepositoryImpl) {
    suspend operator fun invoke(): Flow<UseCaseResult<Double>?> = flow {
        try {
            repository.getTotalPriceOfItemsToBuy().collect { price ->
                if (price != null) {
                    emit(UseCaseResult.Success(price))
                } else {
                    emit(UseCaseResult.Error(Exception("Total price is null")))
                }
            }
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}