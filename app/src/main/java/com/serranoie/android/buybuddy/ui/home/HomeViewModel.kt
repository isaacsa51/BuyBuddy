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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoriesWithItemsUseCase: GetCategoriesWithItemsUseCase,
    private val getTotalPriceOfItemsToBuyUseCase: GetTotalPriceOfItemsToBuyUseCase,
    private val getTotalPriceOfItemsBoughtUseCase: GetTotalPriceOfItemsBoughtUseCase,
    ) : ViewModel() {


    private var _appUnlocked = false

    fun isAppUnlocked(): Boolean = _appUnlocked

    fun setAppUnlocked(unlocked: Boolean) {
        _appUnlocked = unlocked
    }

        private val _categoriesWithItems = MutableStateFlow<List<CategoryWithItemsEntity>>(emptyList())
        val categoriesWithItems: StateFlow<List<CategoryWithItemsEntity>> = _categoriesWithItems

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading

        private val _totalPrice = MutableStateFlow(0.0)
        val totalPrice: StateFlow<Double> = _totalPrice

        private val _totalBoughtPrice = MutableStateFlow(0.0)
        val totalBoughtPrice: StateFlow<Double> = _totalBoughtPrice

        init {
            fetchGetCategoriesWithItems()
            fetchTotalPrices()
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
