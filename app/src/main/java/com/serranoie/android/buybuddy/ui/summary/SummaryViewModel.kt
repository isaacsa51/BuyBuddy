package com.serranoie.android.buybuddy.ui.summary

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
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
    private val _summaryItems = MutableStateFlow<List<ItemEntity>>(emptyList())
    val summaryItems: StateFlow<List<ItemEntity>> = _summaryItems.asStateFlow()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchSummaryItems(month: String) {
        viewModelScope.launch {
            getCurrentMonthSummaryItemsToBuyUseCase(month).collect { result ->
                when (result) {
                    result.isSuccess -> {
                        _summaryItems.value = result.getOrNull() ?: emptyList()
                    }
                    result.isFailure -> {
                        // Handle error
                    }
                }
            }
        }
    }
}