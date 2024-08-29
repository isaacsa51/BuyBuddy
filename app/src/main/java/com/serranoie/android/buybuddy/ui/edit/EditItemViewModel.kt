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
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoryByIdUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.DeleteItemUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetItemByIdUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.UpdateItemStatusUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.UpdateItemUseCase
import com.serranoie.android.buybuddy.ui.core.ScheduleNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    var scheduleNotification: ScheduleNotification = ScheduleNotification()

    private val _isLoading = mutableStateOf(true)
    val isLoading: Boolean
        get() = _isLoading.value

    // Response states
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val _currentItem = mutableStateOf<Item?>(null)
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

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

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
            if (item != null) {
                _currentItem.value = item
                _itemName.value = item.name
                _itemDescription.value = item.description
                _itemPrice.value = item.price
                _itemBenefits.value = item.benefits
                _itemDisadvantages.value = item.disadvantages.toString()
                _selectedDateTime.value = item.reminderDate
                _itemUsage.intValue = mapUsageToStep(item.usage)

                getCategory(item.categoryId)
            } else {
                _isLoading.value = false
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun getCategory(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoryByIdUseCase.getCategoryById(id).collect { category ->
                when(category) {
                    is UseCaseResult.Success -> {
                        _isLoading.value = true
                        _categoryInfo.value = category.data
                        _errorState.value = null
                    }
                    is UseCaseResult.Error -> {
                        _isLoading.value = true
                        _errorState.value = category.exception.message ?: "An error occurred"
                    }
                }
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
            usage = getUsageFromStep(_itemUsage.intValue),
            benefits = _itemBenefits.value,
            disadvantages = _itemDisadvantages.value,
            price = _itemPrice.value ?: 0.0,
            reminderDate = _selectedDateTime.value ?: _currentItem.value?.reminderDate,
            reminderTime = _selectedDateTime.value ?: _currentItem.value?.reminderTime,
            status = _currentItem.value?.status ?: false,
        )

        if (isDateModified || isTimeModified) {
            createUpdatedNotification(updatedItem)
        }

        updateItemUseCase.invoke(updatedItem)
    }

    private fun createUpdatedNotification(updatedItem: Item) {
        scheduleNotification.scheduleNotification(
            context = getApplication<Application>().applicationContext,
            itemId = updatedItem.itemId!!,
            itemName = updatedItem.name,
            reminderDate = updatedItem.reminderDate,
            reminderTime = updatedItem.reminderTime
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun deleteItem(id: Int) {
        when(val result = deleteItemUseCase(id)) {
            is UseCaseResult.Success -> {
            }

            is UseCaseResult.Error -> {
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    suspend fun updateItemStatus(
        id: Int,
        status: Boolean,
    ) {
        updateItemStatusUseCase.invoke(id, status)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun mapUsageToStep(usage: String): Int {
        val context = getApplication<Application>().applicationContext

        return when (usage) {
            context.getString(R.string.usage_barely) -> steps.indexOf(R.string.usage_barely)

            context.getString(R.string.usage_rarely) -> steps.indexOf(R.string.usage_rarely)

            context.getString(R.string.usage_ocasionally) -> steps.indexOf(R.string.usage_ocasionally)

            context.getString(R.string.usage_sometimes) -> steps.indexOf(R.string.usage_sometimes)

            context.getString(R.string.usage_often) -> steps.indexOf(R.string.usage_often)

            context.getString(R.string.usage_almost_everyday) -> steps.indexOf(R.string.usage_almost_everyday)
            else -> {
                steps.indexOf(R.string.usage_barely)
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
