package com.serranoie.android.buybuddy.ui.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoryByIdUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.DeleteItemUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetItemByIdUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.UpdateItemStatusUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.UpdateItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel
    @Inject
    constructor(
        private val getItemByIdUseCase: GetItemByIdUseCase,
        private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
        private val deleteItemUseCase: DeleteItemUseCase,
        private val updateItemUseCase: UpdateItemUseCase,
        private val updateItemStatusUseCase: UpdateItemStatusUseCase,
    ) : ViewModel() {
        private val _currentItem = MutableStateFlow<Item?>(null)
        val currentItem: StateFlow<Item?> = _currentItem.asStateFlow()

        private val _categoryInfo = MutableStateFlow<Category?>(null)
        val categoryInfo: StateFlow<Category?> = _categoryInfo.asStateFlow()

        private val _itemName = MutableStateFlow("")
        val itemName: StateFlow<String> = _itemName.asStateFlow()

        private val _itemDescription = MutableStateFlow("")
        val itemDescription: StateFlow<String> = _itemDescription.asStateFlow()

        private val _itemPrice = MutableStateFlow<Double?>(null)
        val itemPrice: StateFlow<Double?> = _itemPrice.asStateFlow()

        private val _itemBenefits = MutableStateFlow("")
        val itemBenefits: StateFlow<String> = _itemBenefits.asStateFlow()

        private val _itemDisadvantages = MutableStateFlow("")
        val itemDisadvantages: StateFlow<String> = _itemDisadvantages.asStateFlow()

        private val _selectedDateTime = MutableStateFlow<Date?>(null)
        val selectedDateTime: StateFlow<Date?> = _selectedDateTime.asStateFlow()

        fun updateItemName(newName: String) {
            _itemName.value = newName
        }

        fun updateItemDescription(newDescription: String) {
            _itemDescription.value = newDescription
        }

        fun updateItemPrice(newPrice: Double) {
            _itemPrice.value = newPrice
        }

        fun updateItemBenefits(newBenefits: String) {
            _itemBenefits.value = newBenefits
        }

        fun updateItemDisadvantages(newDisadvantages: String) {
            _itemDisadvantages.value = newDisadvantages
        }

        fun updateSelectedDateTime(selectedDate: Date) {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            calendar.time = selectedDate
            _selectedDateTime.value = calendar.time
        }

        suspend fun getItemById(id: Int) {
            getItemByIdUseCase.invoke(id).collect { item ->
                _currentItem.value = item
                _itemName.value = item!!.name
                _itemDescription.value = item.description
                _itemPrice.value = item.price
                _itemBenefits.value = item.benefits
                _itemDisadvantages.value = item.disadvantages.toString()

                getCategory(item.categoryId)
            }
        }

        suspend fun getCategory(id: Int) {
            viewModelScope.launch {
                getCategoryByIdUseCase.getCategoryById(id).collect { category ->
                    Log.d("DEBUG", "Fetched Category: $category")
                    _categoryInfo.value = category
                }
            }
        }

        // Function to update the item
        suspend fun updateItem(itemId: Int) {
//        val updatedItem = Item(
//            itemId = itemId,
//            name = _itemName.value,
//            description = _itemDescription.value,
//            price = _itemPrice.value,
//        )
//        updateItemUseCase.invoke(updatedItem)
        }

        suspend fun deleteItem(
            id: Int,
            navController: NavController,
        ) {
            deleteItemUseCase.deleteItem(id)
            navController.popBackStack()
        }

        suspend fun updateItemStatus(
            id: Int,
            status: Boolean,
        ) {
            updateItemStatusUseCase.invoke(id, status)
        }
    }
