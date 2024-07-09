package com.serranoie.android.buybuddy.ui.edit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoryByIdUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.DeleteItemUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetItemByIdUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.UpdateItemStatusUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.UpdateItemUseCase
import com.serranoie.android.buybuddy.ui.core.ScheduleNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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
        application: Application
    ) : AndroidViewModel(application) {
        private val steps =
            listOf(
                R.string.usage_barely,
                R.string.usage_rarely,
                R.string.usage_ocasionally,
                R.string.usage_sometimes,
                R.string.usage_often,
                R.string.usage_almost_everyday,
            )

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

        private val _itemUsage = MutableStateFlow(0)
        val itemUsage: StateFlow<Int> = _itemUsage.asStateFlow()

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

        fun updateItemUsage(newUsage: Int) {
            _itemUsage.value = newUsage
        }

        suspend fun getItemById(id: Int) {
            getItemByIdUseCase.invoke(id).collect { item ->
                _currentItem.value = item
                _itemName.value = item!!.name
                _itemDescription.value = item.description
                _itemPrice.value = item.price
                _itemBenefits.value = item.benefits
                _itemDisadvantages.value = item.disadvantages.toString()
                _itemUsage.value = mapUsageToStep(item.usage)
                _selectedDateTime.value = item.reminderDate

                getCategory(item.categoryId)
            }
        }

        private fun mapUsageToStep(usage: String): Int =
            when (usage) {
                "Barely" -> steps.indexOf(R.string.usage_barely)
                "Rarely" -> steps.indexOf(R.string.usage_rarely)
                "Occasionally" -> steps.indexOf(R.string.usage_ocasionally)
                "Sometimes" -> steps.indexOf(R.string.usage_sometimes)
                "Often" -> steps.indexOf(R.string.usage_often)
                "Almost Everyday" -> steps.indexOf(R.string.usage_almost_everyday)
                else -> 0 // Default to the first step if usage is unknown
            }

        suspend fun getCategory(id: Int) {
            viewModelScope.launch {
                getCategoryByIdUseCase.getCategoryById(id).collect { category ->
                    _categoryInfo.value = category
                }
            }
        }

        suspend fun updateItem(itemId: Int) {
            val isDateModified = _selectedDateTime.value != _currentItem.value?.reminderDate
            val isTimeModified = _selectedDateTime.value != _currentItem.value?.reminderTime

            val currentCategoryId = _currentItem.value?.categoryId ?: return

            val updatedItem =
                Item(
                    itemId = itemId,
                    name = _itemName.value,
                    categoryId = currentCategoryId,
                    description = _itemDescription.value,
                    usage = getUsageFromStep(_itemUsage.value),
                    benefits = _itemBenefits.value,
                    disadvantages = _itemDisadvantages.value,
                    price = _itemPrice.value!!,
                    reminderDate = _selectedDateTime.value ?: _currentItem.value?.reminderDate,
                    reminderTime = _selectedDateTime.value ?: _currentItem.value?.reminderTime,
                    status = _currentItem.value?.status ?: false,
                )

            if(isDateModified || isTimeModified) {
                updatedItem.let {
                    ScheduleNotification().scheduleNotification(
                        context = getApplication<Application>().applicationContext,
                        itemId = updatedItem.itemId!!,
                        itemName = updatedItem.name,
                        reminderDate = updatedItem.reminderDate,
                        reminderTime = updatedItem.reminderTime
                    )

                }
            }

            updateItemUseCase.invoke(updatedItem)
        }

        private fun getUsageFromStep(stepIndex: Int): String =
            when (steps[stepIndex]) {
                R.string.usage_barely -> "Barely"
                R.string.usage_rarely -> "Rarely"
                R.string.usage_ocasionally -> "Occasionally"
                R.string.usage_sometimes -> "Sometimes"
                R.string.usage_often -> "Often"
                R.string.usage_almost_everyday -> "Almost Everyday"
                else -> "Unknown"
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
