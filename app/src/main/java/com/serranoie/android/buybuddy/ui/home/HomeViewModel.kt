package com.serranoie.android.buybuddy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.domain.model.CategoryWithItems
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

    private fun fetchGetCategoriesWithItems(){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            getCategoriesWithItemsUseCase().collect { categoriesWithItems ->
                _categoriesWithItems.value = categoriesWithItems
                _isLoading.value = false
            }
        }
    }

    private fun fetchTotalPrices() {
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