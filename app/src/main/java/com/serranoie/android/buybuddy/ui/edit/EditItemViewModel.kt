package com.serranoie.android.buybuddy.ui.edit

import androidx.lifecycle.ViewModel
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
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(
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

    suspend fun getItemById(id: Int) {
        getItemByIdUseCase.invoke(id).collect { item ->
            _currentItem.value = item
        }
    }

    private suspend fun getCategory(id: Int) {
        getCategoryByIdUseCase.getCategoryById(id).collect { category ->
            _categoryInfo.value = category
        }
    }

    suspend fun deleteItem(id: Int, navController: NavController) {
        deleteItemUseCase.deleteItem(id)
        navController.popBackStack()
    }

    suspend fun updateItemStatus(id: Int, status: Boolean) {
        updateItemStatusUseCase.invoke(id, status)
    }
}