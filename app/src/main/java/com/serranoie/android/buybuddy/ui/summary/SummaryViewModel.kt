package com.serranoie.android.buybuddy.ui.summary

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.buybuddy.domain.model.ItemPrice
import com.serranoie.android.buybuddy.domain.model.MonthlySum
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import com.serranoie.android.buybuddy.domain.usecase.item.GetCurrentMonthSummaryItemsBoughtUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetCurrentMonthSummaryItemsToBuyUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetYearlySummaryBoughtUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetYearlySummaryToBuyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val getCurrentMonthSummaryItemsToBuyUseCase: GetCurrentMonthSummaryItemsToBuyUseCase,
    private val getCurrentMonthSummaryItemsBoughtUseCase: GetCurrentMonthSummaryItemsBoughtUseCase,
    private val getYearlySummaryToBuyUseCase: GetYearlySummaryToBuyUseCase,
    private val getYearlySummaryBoughtUseCase: GetYearlySummaryBoughtUseCase,
) : ViewModel() {

    // Response states
    private val _summaryItemsToBuy = MutableStateFlow<List<ItemPrice>?>(emptyList())
    val summaryItemsToBuy: StateFlow<List<ItemPrice>?> = _summaryItemsToBuy.asStateFlow()

    private val _summaryItemsBought = MutableStateFlow<List<ItemPrice>?>(emptyList())
    val summaryItemsBought: StateFlow<List<ItemPrice>?> = _summaryItemsBought.asStateFlow()

    private val _yearlySummaryToBuy = MutableStateFlow<List<MonthlySum>?>(emptyList())
    val yearlySummaryToBuy: StateFlow<List<MonthlySum>?> = _yearlySummaryToBuy.asStateFlow()

    private val _yearlySummaryBought = MutableStateFlow<List<MonthlySum>?>(emptyList())
    val yearlySummaryBought: StateFlow<List<MonthlySum>?> = _yearlySummaryBought.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchSummaryItemsToBuy(month: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentMonthSummaryItemsToBuyUseCase(month).collect { result ->
                when (result) {
                    is UseCaseResult.Success<*> -> {
                        withContext(Dispatchers.Main) {
                            _summaryItemsToBuy.value = result.data as List<ItemPrice>
                            _errorState.value = null
                        }
                    }

                    is UseCaseResult.Error -> {
                        _errorState.value = result.exception.message ?: "An error occurred"
                    }

                    null -> {
                        _errorState.value = "No data found"
                    }
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchSummaryItemsBought(month: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentMonthSummaryItemsBoughtUseCase(month).collect { result ->
                when (result) {
                    is UseCaseResult.Success<*> -> {
                        withContext(Dispatchers.Main) {
                            _summaryItemsBought.value = result.data as List<ItemPrice>
                            _errorState.value = null
                        }
                    }

                    is UseCaseResult.Error -> {
                        _errorState.value = result.exception.message ?: "An error occurred"
                    }

                    null -> {
                        _errorState.value = "No data found"
                    }
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchYearlySummaryToBuy() {
        viewModelScope.launch(Dispatchers.IO) {
            getYearlySummaryToBuyUseCase().collect { result ->
                when (result) {
                    is UseCaseResult.Success<*> -> {
                        withContext(Dispatchers.Main) {
                            _yearlySummaryToBuy.value = result.data as List<MonthlySum>
                            _errorState.value = null
                        }
                    }

                    is UseCaseResult.Error -> {
                        _errorState.value = result.exception.message ?: "An error occurred"
                    }

                    null -> {
                        _errorState.value = "No data found"
                    }
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchYearlySummaryBought() {
        viewModelScope.launch(Dispatchers.IO) {
            getYearlySummaryBoughtUseCase().collect { result ->
                when (result) {
                    is UseCaseResult.Success<*> -> {
                        withContext(Dispatchers.Main) {
                            _yearlySummaryBought.value = result.data as List<MonthlySum>
                            _errorState.value = null
                        }
                    }

                    is UseCaseResult.Error -> {
                        _errorState.value = result.exception.message ?: "An error occurred"
                    }

                    null -> {
                        _errorState.value = "No data found"
                    }
                }
            }
        }
    }
}