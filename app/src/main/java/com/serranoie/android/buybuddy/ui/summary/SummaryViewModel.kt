package com.serranoie.android.buybuddy.ui.summary

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.buybuddy.data.persistance.entity.ItemPriceEntity
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import com.serranoie.android.buybuddy.domain.usecase.item.GetCurrentMonthSummaryItemsToBuyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val getCurrentMonthSummaryItemsToBuyUseCase: GetCurrentMonthSummaryItemsToBuyUseCase
) : ViewModel() {

    // Response states
    private val _summaryItemsToBuy = MutableStateFlow<List<ItemPriceEntity>>(emptyList())
    val summaryItemsToBuy: StateFlow<List<ItemPriceEntity>> = _summaryItemsToBuy.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchSummaryItemsToBuy(month: String) {
        viewModelScope.launch {
            getCurrentMonthSummaryItemsToBuyUseCase(month).collect { result ->
                when (result) {
                    is UseCaseResult.Success<*> -> {
                        _summaryItemsToBuy.value = result.data as List<ItemPriceEntity>
                        _errorState.value = null
                    }

                    is UseCaseResult.Error -> {
                        _errorState.value = result.exception.message ?: "An error occurred"
                    }
                }
            }
        }
    }
}