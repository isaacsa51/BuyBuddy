package com.serranoie.android.buybuddy.ui.summary

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusOne
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusZero
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusOne
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusZero
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusOne
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusZero
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import com.serranoie.android.buybuddy.domain.usecase.category.GetMonthlySumCategoryStatusOneUseCase
import com.serranoie.android.buybuddy.domain.usecase.category.GetMonthlySumCategoryStatusZeroUseCase
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
    private val getMonthlySumCategoryStatusZero: GetMonthlySumCategoryStatusZeroUseCase,
    private val getMonthlySumCategoryStatusOne: GetMonthlySumCategoryStatusOneUseCase,
) : ViewModel() {

    // Response states
    private val _summaryItemsToBuy = MutableStateFlow<List<ItemPriceStatusZero>?>(emptyList())
    val summaryItemsToBuy: StateFlow<List<ItemPriceStatusZero>?> = _summaryItemsToBuy.asStateFlow()

    private val _summaryItemsBought = MutableStateFlow<List<ItemPriceStatusOne>?>(emptyList())
    val summaryItemsBought: StateFlow<List<ItemPriceStatusOne>?> = _summaryItemsBought.asStateFlow()

    private val _yearlySummaryToBuy = MutableStateFlow<List<MonthlySumStatusZero>?>(emptyList())
    val yearlySummaryToBuy: StateFlow<List<MonthlySumStatusZero>?> =
        _yearlySummaryToBuy.asStateFlow()

    private val _yearlySummaryBought = MutableStateFlow<List<MonthlySumStatusOne>?>(emptyList())
    val yearlySummaryBought: StateFlow<List<MonthlySumStatusOne>?> =
        _yearlySummaryBought.asStateFlow()

    private val _monthlyCategorySumToBuy =
        MutableStateFlow<List<MonthlySumCategoryStatusZero>?>(emptyList())
    val monthlyCategorySumToBuy: StateFlow<List<MonthlySumCategoryStatusZero>?> =
        _monthlyCategorySumToBuy.asStateFlow()

    private val _monthlyCategorySumBought =
        MutableStateFlow<List<MonthlySumCategoryStatusOne>?>(emptyList())
    val monthlyCategorySumBought: StateFlow<List<MonthlySumCategoryStatusOne>?> =
        _monthlyCategorySumBought.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchSummaryItemsToBuy(month: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentMonthSummaryItemsToBuyUseCase(month).collect { result ->
                when (result) {
                    is UseCaseResult.Success<*> -> {
                        withContext(Dispatchers.Main) {
                            _summaryItemsToBuy.value = result.data as List<ItemPriceStatusZero>
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
                            _summaryItemsBought.value = result.data as List<ItemPriceStatusOne>
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
                            _yearlySummaryToBuy.value = result.data as List<MonthlySumStatusZero>
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
                            _yearlySummaryBought.value = result.data as List<MonthlySumStatusOne>
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
    fun fetchMonthlyCategorySumToBuy(month: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getMonthlySumCategoryStatusZero(month).collect { result ->
                when (result) {
                    is UseCaseResult.Success<*> -> {
                        withContext(Dispatchers.Main) {
                            _monthlyCategorySumToBuy.value =
                                result.data as List<MonthlySumCategoryStatusZero>
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
    fun fetchMonthlyCategorySumBought(month: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getMonthlySumCategoryStatusOne(month).collect { result ->
                when (result) {
                    is UseCaseResult.Success<*> -> {
                        withContext(Dispatchers.Main) {
                            _monthlyCategorySumBought.value =
                                result.data as List<MonthlySumCategoryStatusOne>
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