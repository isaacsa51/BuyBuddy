package com.serranoie.android.buybuddy.ui.edit

import android.app.Application
import com.serranoie.android.buybuddy.domain.model.Category
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.usecase.category.GetCategoryByIdUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.DeleteItemUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.GetItemByIdUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.UpdateItemStatusUseCase
import com.serranoie.android.buybuddy.domain.usecase.item.UpdateItemUseCase
import com.serranoie.android.buybuddy.ui.core.ScheduleNotification
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.Date

@ExperimentalCoroutinesApi
class EditItemViewModelTest {
    @MockK
    private lateinit var getItemByIdUseCase: GetItemByIdUseCase

    @MockK
    private lateinit var getCategoryByIdUseCase: GetCategoryByIdUseCase

    @MockK
    private lateinit var deleteItemUseCase: DeleteItemUseCase

    @MockK
    private lateinit var updateItemUseCase: UpdateItemUseCase

    @MockK
    private lateinit var updateItemStatusUseCase: UpdateItemStatusUseCase

    @MockK
    private lateinit var application: Application

    private lateinit var viewModel: EditItemViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every { application.getString(any()) } returns ""
        viewModel = EditItemViewModel(
            getItemByIdUseCase,
            getCategoryByIdUseCase,
            deleteItemUseCase,
            updateItemUseCase,
            updateItemStatusUseCase,
            application
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getItemById success`() = runTest {
        // Given
        val itemId = 1
        val item = Item(
            itemId = itemId,
            categoryId = 1,
            name = "Test Item",
            description = "Test Description",
            price = 10.0,
            usage = "Often",
            benefits = "Test Benefits",
            disadvantages = "Test Disadvantages",
            reminderDate = Date(),
            reminderTime = Date(),
            status = false
        )
        val category = Category(categoryId = 1, name = "Test Category")

        coEvery { getItemByIdUseCase.invoke(itemId) } returns flowOf(item)
        coEvery { getCategoryByIdUseCase.getCategoryById(item.categoryId) } returns flowOf(category)
        every { application.applicationContext } returns application

        // When
        viewModel.triggerProductData(itemId)

        // Then
        assertEquals(item, viewModel.currentItem)
        assertEquals(item.name, viewModel.itemName)
        assertEquals(item.description, viewModel.itemDescription)
        assertEquals(item.price, viewModel.itemPrice)
        assertEquals(item.benefits, viewModel.itemBenefits)
        assertEquals(item.disadvantages.toString(), viewModel.itemDisadvantages)
        assertEquals(item.reminderDate, viewModel.selectedDateTime)
        assertEquals(viewModel.mapUsageToStep(item.usage), viewModel.itemUsage)
        assertEquals(category, viewModel.categoryInfo)
    }

    @Test
    fun `test getItemById not found`() = runTest {
        // Given
        val itemId = 1
        coEvery { getItemByIdUseCase.invoke(itemId) } returns flowOf(null)

        // When
        viewModel.triggerProductData(itemId)

        // Then
        assertEquals(null, viewModel.currentItem)
        assertEquals(false, viewModel.isLoading)
    }

    @Test
    fun `test updateItem with date and time modified`() = runTest {
        // Given
        val itemId = 1
        val originalItem = Item(
            itemId = itemId,
            categoryId = 1,
            name = "Test Item",
            description = "Test Description",
            price = 10.0,
            usage = "Often",
            benefits = "Test Benefits",
            disadvantages = "Test Disadvantages",
            reminderDate = Date(),
            reminderTime = Date(),
            status = false
        )

        val scheduleNotificationMock = mockk<ScheduleNotification>()

        val newDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }.time

        val viewModelWithMock = EditItemViewModel( // Create viewModelWithMock
            getItemByIdUseCase,
            getCategoryByIdUseCase,
            deleteItemUseCase,
            updateItemUseCase,
            updateItemStatusUseCase,
            application
        ).apply {
            this.scheduleNotification = scheduleNotificationMock
        }

        viewModelWithMock._currentItem.value = originalItem
        viewModelWithMock.onItemNameResponse("Updated Name")
        viewModelWithMock.onItemDescriptionResponse("Updated Description")
        viewModelWithMock.onItemPriceResponse(15.0)
        viewModelWithMock.onItemBenefitsResponse("Updated Benefits")
        viewModelWithMock.onItemDisadvantagesResponse("Updated Disadvantages")
        viewModelWithMock.onSelectedDateTimeResponse(newDate)
        viewModelWithMock.onItemUsageResponse(2)

        coEvery { updateItemUseCase.invoke(any()) } just runs

        every { application.applicationContext } returns application
        every { scheduleNotificationMock.scheduleNotification(any(), any(), any(), any(), any()) } just runs

        // When
        viewModelWithMock.updateItem(itemId)

        // Then
        val expectedUpdatedItem = Item(
            itemId = itemId,
            categoryId = 1,
            name = "Updated Name",
            description = "Updated Description",
            price = 15.0,
            usage = "Occasionally",
            benefits = "Updated Benefits",
            disadvantages = "Updated Disadvantages",
            reminderDate = newDate,
            reminderTime = newDate,
            status = false
        )

        coVerify { updateItemUseCase.invoke(expectedUpdatedItem) }

        verify {
            scheduleNotificationMock.scheduleNotification(
                application,
                expectedUpdatedItem.itemId!!,
                expectedUpdatedItem.name,
                expectedUpdatedItem.reminderDate,
                expectedUpdatedItem.reminderTime
            )
        }
    }

    @Test
    fun `test deleteItem`() = runTest {
        // Given
        val itemId = 1

        coEvery { deleteItemUseCase.deleteItem(itemId) } just runs

        // When
        viewModel.deleteItem(itemId)

        // Then
        coVerify { deleteItemUseCase.deleteItem(itemId) }
    }

    @Test
    fun `test updateItemStatus`() = runTest {
        // Given
        val itemId = 1
        val newStatus = true

        coEvery { updateItemStatusUseCase.invoke(itemId, newStatus) } just runs

        // When
        viewModel.updateItemStatus(itemId, newStatus)

        // Then
        coVerify { updateItemStatusUseCase.invoke(itemId, newStatus) }
    }
}