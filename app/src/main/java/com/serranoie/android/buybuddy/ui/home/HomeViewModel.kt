package com.serranoie.android.buybuddy.ui.home

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoriesWithItemsUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetTotalPriceOfItemsBoughtUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetTotalPriceOfItemsToBuyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesWithItemsUseCase: GetCategoriesWithItemsUseCase,
    private val getTotalPriceOfItemsToBuyUseCase: GetTotalPriceOfItemsToBuyUseCase,
    private val getTotalPriceOfItemsBoughtUseCase: GetTotalPriceOfItemsBoughtUseCase,
) : ViewModel() {

    // TODO: Implement Biometrics authentication
    private var _appUnlocked = false

    fun isAppUnlocked(): Boolean = _appUnlocked

    fun setAppUnlocked(unlocked: Boolean) {
        _appUnlocked = unlocked
    }

    private val _triggerDataFetch = MutableStateFlow(false)
    val triggerDataFetch: StateFlow<Boolean> = _triggerDataFetch

    // Response states
    private val _categoriesWithItems = MutableStateFlow<List<CategoryWithItemsEntity>>(emptyList())
    val categoriesWithItems: StateFlow<List<CategoryWithItemsEntity>> =
        _categoriesWithItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice = _totalPrice.asStateFlow()

    private val _totalBoughtPrice = MutableStateFlow(0.0)
    val totalBoughtPrice: StateFlow<Double> = _totalBoughtPrice.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    fun triggerDataFetch() {
        _triggerDataFetch.value = true
    }

    init {
        viewModelScope.launch {
            triggerDataFetch.collect { shouldFetch ->
                if (shouldFetch) {
                    fetchGetCategoriesWithItems()
                    fetchTotalPrices()
                    _triggerDataFetch.value = false
                    _isLoading.value = false
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchGetCategoriesWithItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoriesWithItemsUseCase.invoke().collect { categoriesWithItems ->
                when (categoriesWithItems) {
                    is UseCaseResult.Success<*> -> {
                        withContext(Dispatchers.Main) {
                            _isLoading.value = true
                            _categoriesWithItems.value =
                                categoriesWithItems.data as List<CategoryWithItemsEntity>
                            _errorState.value = null
                        }
                    }

                    is UseCaseResult.Error -> {
                        _isLoading.value = true
                        _errorState.value =
                            categoriesWithItems.exception.message ?: "An error occurred"
                    }
                }

            }
            _isLoading.value = false
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchTotalPrices() {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                getTotalPriceOfItemsBoughtUseCase().collect { result ->
                    when (result) {
                        is UseCaseResult.Success -> {
                            _totalBoughtPrice.value = result.data
                        }

                        is UseCaseResult.Error -> {
                            _totalBoughtPrice.value = 0.0
                            Log.e(
                                "DEBUG",
                                "Error fetching total bought price: ${result.exception.message}"
                            )
                        }
                    }
                }
            }
            launch {
                getTotalPriceOfItemsToBuyUseCase().collect { result ->
                    when (result) {
                        is UseCaseResult.Success -> {
                            _totalPrice.value = result.data
                        }

                        is UseCaseResult.Error -> {
                            _totalPrice.value = 0.0
                            Log.e(
                                "DEBUG",
                                "Error fetching total price: ${result.exception.message}"
                            )
                        }

                        null -> TODO()
                    }
                }
            }
        }
    }
}
