package com.serranoie.android.buybuddy.ui.edit

import android.app.Application
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel
@Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val updateItemUseCase: UpdateItemUseCase,
    private val updateItemStatusUseCase: UpdateItemStatusUseCase,
    application: Application
) : AndroidViewModel(application) {
    private val steps = listOf(
        R.string.usage_barely,
        R.string.usage_rarely,
        R.string.usage_ocasionally,
        R.string.usage_sometimes,
        R.string.usage_often,
        R.string.usage_almost_everyday,
    )

    private val _isLoading = mutableStateOf(true)
    val isLoading: Boolean
        get() = _isLoading.value

    // Response states
    private val _currentItem = mutableStateOf<Item?>(null)
    val currentItem: Item?
        get() = _currentItem.value

    private val _categoryInfo = mutableStateOf<Category?>(null)
    val categoryInfo: Category?
        get() = _categoryInfo.value

    private val _itemName = mutableStateOf("")
    val itemName: String
        get() = _itemName.value

    private val _itemDescription = mutableStateOf("")
    val itemDescription: String
        get() = _itemDescription.value

    private val _itemPrice = mutableStateOf<Double?>(null)
    val itemPrice: Double?
        get() = _itemPrice.value

    private val _itemBenefits = mutableStateOf("")
    val itemBenefits: String
        get() = _itemBenefits.value

    private val _itemDisadvantages = mutableStateOf("")
    val itemDisadvantages: String
        get() = _itemDisadvantages.value

    private val _selectedDateTime = mutableStateOf<Date?>(null)
    val selectedDateTime: Date?
        get() = _selectedDateTime.value

    private val _itemUsage = mutableIntStateOf(0)
    val itemUsage: Int
        get() = _itemUsage.intValue

    // Exposing states to UI
    fun onItemNameResponse(name: String) {
        _itemName.value = name
    }

    fun onItemDescriptionResponse(description: String) {
        _itemDescription.value = description
    }

    fun onItemPriceResponse(price: Double) {
        _itemPrice.value = price
    }

    fun onItemBenefitsResponse(benefits: String) {
        _itemBenefits.value = benefits
    }

    fun onItemDisadvantagesResponse(disadvantages: String) {
        _itemDisadvantages.value = disadvantages
    }

    fun onSelectedDateTimeResponse(selectedDate: Date) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.time = selectedDate
        _selectedDateTime.value = calendar.time
    }

    fun onItemUsageResponse(usage: Int) {
        _itemUsage.intValue = usage
    }

    suspend fun triggerProductData(id: Int) {
        viewModelScope.launch {
            getItemById(id)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun getItemById(id: Int) {
        _isLoading.value = true

        getItemByIdUseCase.invoke(id).collect { item ->
            if (item != null) { // Check if item is not null
                _currentItem.value = item
                _itemName.value = item.name
                _itemDescription.value = item.description
                // ... set other properties ...
                getCategory(item.categoryId)
            } else {
                // Handle the case where the item is not found
                // For example,you could:
                // - Show an error message to the user
                // - Navigate back to the previous screen
                // - Set a flag to indicate that the item was not found
                _isLoading.value = false // Stop loading state
                // ... other actions ...
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun getCategory(id: Int) {
        viewModelScope.launch {
            getCategoryByIdUseCase.getCategoryById(id).collect { category ->
                _categoryInfo.value = category
            }
        }

        delay(80)
        _isLoading.value = false
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun updateItem(itemId: Int) {
        val isDateModified = _selectedDateTime.value != _currentItem.value?.reminderDate
        val isTimeModified = _selectedDateTime.value != _currentItem.value?.reminderTime

        val currentCategoryId = _currentItem.value?.categoryId ?: return

        val updatedItem = Item(
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

        if (isDateModified || isTimeModified) {
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun deleteItem(id: Int) {
        deleteItemUseCase.deleteItem(id)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun updateItemStatus(
        id: Int,
        status: Boolean,
    ) {
        updateItemStatusUseCase.invoke(id, status)
    }

    private fun mapUsageToStep(usage: String): Int {
        val context = getApplication<Application>().applicationContext
        val lowerCaseUsage = usage.lowercase()

        return when (lowerCaseUsage) {
            context.getString(R.string.usage_barely)
                .lowercase() -> steps.indexOf(R.string.usage_barely)

            context.getString(R.string.usage_rarely)
                .lowercase() -> steps.indexOf(R.string.usage_rarely)

            context.getString(R.string.usage_ocasionally)
                .lowercase() -> steps.indexOf(R.string.usage_ocasionally)

            context.getString(R.string.usage_sometimes)
                .lowercase() -> steps.indexOf(R.string.usage_sometimes)

            context.getString(R.string.usage_often)
                .lowercase() -> steps.indexOf(R.string.usage_often)

            context.getString(R.string.usage_almost_everyday)
                .lowercase() -> steps.indexOf(R.string.usage_almost_everyday)

            else -> {
                Log.w("EditItemViewModel", "Unknown usage string: $usage")
                0
            }
        }
    }

    private fun getUsageFromStep(stepIndex: Int): String = when (steps[stepIndex]) {
        R.string.usage_barely -> "Barely"
        R.string.usage_rarely -> "Rarely"
        R.string.usage_ocasionally -> "Occasionally"
        R.string.usage_sometimes -> "Sometimes"
        R.string.usage_often -> "Often"
        R.string.usage_almost_everyday -> "Almost Everyday"
        else -> "Unknown"
    }
}
