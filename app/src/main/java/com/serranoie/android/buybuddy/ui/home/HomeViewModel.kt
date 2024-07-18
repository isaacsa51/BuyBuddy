package com.serranoie.android.buybuddy.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoriesWithItemsUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetTotalPriceOfItemsBoughtUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetTotalPriceOfItemsToBuyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
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
    private val _categoriesWithItems = mutableStateOf<List<CategoryWithItemsEntity>>(emptyList())
    val categoriesWithItems: List<CategoryWithItemsEntity>
        get() = _categoriesWithItems.value

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading.value

    private val _totalPrice = mutableStateOf(0.0)
    val totalPrice: Double
        get() = _totalPrice.value

    private val _totalBoughtPrice = mutableStateOf(0.0)
    val totalBoughtPrice: Double
        get() = _totalBoughtPrice.value

    // States exposed to UI
    fun setCategoriesWithItems(categoriesWithItems: List<CategoryWithItemsEntity>) {
        _categoriesWithItems.value = categoriesWithItems
    }

    fun setIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun setTotalPrice(totalPrice: Double) {
        _totalPrice.value = totalPrice
    }

    fun setTotalBoughtPrice(totalBoughtPrice: Double) {
        _totalBoughtPrice.value = totalBoughtPrice
    }

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
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchGetCategoriesWithItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            getCategoriesWithItemsUseCase.invoke().collect { categoriesWithItems ->
                _categoriesWithItems.value = categoriesWithItems
                _isLoading.value = false
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun fetchTotalPrices() {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                getTotalPriceOfItemsBoughtUseCase().collect { totalBoughtPrice ->
                    _totalBoughtPrice.value = totalBoughtPrice ?: 0.0
                }
            }
            launch {
                getTotalPriceOfItemsToBuyUseCase().collect { totalPrice ->
                    _totalPrice.value = totalPrice ?: 0.0
                }
            }
        }
    }
}
