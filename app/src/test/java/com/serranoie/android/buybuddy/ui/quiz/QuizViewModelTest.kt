package com.serranoie.android.buybuddy.ui.quiz

import android.app.Application
import com.serranoie.android.buybuddy.domain.usecase.item.InsertItemWithCategoryUseCase
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.quiz.common.Questions
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    @MockK
    private lateinit var insertItemWithCategoryUseCase: InsertItemWithCategoryUseCase

    @MockK
    private lateinit var application: Application

    @MockK
    private lateinit var userEventsTracker: UserEventsTracker

    private lateinit var viewModel: QuizViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = QuizViewModel(insertItemWithCategoryUseCase, userEventsTracker, application)

        every { application.getString(any<Int>()) } returns ""
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial state`() {
        assertEquals(0, viewModel.quizScreenData.questionIndex)
        assertEquals(6, viewModel.quizScreenData.questionCount)
        assertFalse(viewModel.quizScreenData.shouldShowPreviousButton)
        assertFalse(viewModel.quizScreenData.shouldShowDoneButton)
        assertEquals(Questions.NAME, viewModel.quizScreenData.currentQuestion)
        assertFalse(viewModel.isNextEnabled)
    }

    @Test
    fun `test onNameResponse enables next when name, description, and price are filled`() {
        viewModel.onNameResponse("Test Item")
        viewModel.onDescriptionResponse("Test Description")
        viewModel.onPriceResponse("10.0")
        assertTrue(viewModel.isNextEnabled)
    }

    @Test
    fun `test onBackPressed returns false when on first question`() {
        assertFalse(viewModel.onBackPressed())
    }

    @Test
    fun `test onBackPressed returns true and changes question when not on firstquestion`() {
        viewModel.onNextPressed() // Move to the next question
        assertTrue(viewModel.onBackPressed())
        assertEquals(0, viewModel.quizScreenData.questionIndex)
    }

    @Test(expected = IllegalStateException::class)
    fun `test onPreviousPressed throws exception when on first question`() {
        viewModel.onPreviousPressed()
    }

    @Test
    fun `test onPreviousPressed changes question when not on first question`() {
        viewModel.onNextPressed() // Move to the next question
        viewModel.onPreviousPressed()
        assertEquals(0, viewModel.quizScreenData.questionIndex)
    }

    @Test
    fun `test onNextPressed changes question`() {
        viewModel.onNextPressed()
        assertEquals(1, viewModel.quizScreenData.questionIndex)
    }

//    @Test
//    fun `test onDonePressed inserts item and schedules notification`() = runTest {
//        // Given
//        val category = Category(categoryId = 1, name = 1)
//        val reminderDate = Calendar.getInstance().time
//
//        val scheduleNotificationMock = mockk<ScheduleNotification>()
//        every {
//            scheduleNotificationMock.scheduleNotification(
//                any(),
//                any(),
//                any(),
//                any(),
//                any()
//            )
//        } just runs
//
//        val viewModelWithMock = QuizViewModel(
//            insertItemWithCategoryUseCase,
//            application
//        )
//
//        viewModelWithMock.onNameResponse("Test Item")
//        viewModelWithMock.onDescriptionResponse("Test Description")
//        viewModelWithMock.onPriceResponse("10.0")
//        viewModelWithMock.onCategoryResponse(category)
//        viewModelWithMock.onUsageResponse(1)
//        viewModelWithMock.onBenefitsResponse("Test Benefits")
//        viewModelWithMock.onContrasResponse("Test Contras")
//        viewModelWithMock.onReminderResponse(reminderDate)
//
//        val onSurveyCompleteSlot = slot<() -> Unit>()
//        coEvery { insertItemWithCategoryUseCase(any(), any()) } coAnswers {
//            onSurveyCompleteSlot.captured()
//        }
//
//        var surveyCompleted = false
//        val onSurveyComplete = { surveyCompleted = true }
//
//        // When
//        viewModelWithMock.onDonePressed(onSurveyComplete)
//        advanceUntilIdle()
//
//        // Then
//        val expectedItem = Item(
//            itemId = null,
//            name = "Test Item",
//            categoryId = 1,
//            description = "Test Description",
//            usage = "",
//            benefits = "Test Benefits",
//            disadvantages = "Test Contras",
//            reminderDate = reminderDate,
//            reminderTime = reminderDate,
//            price = 10.0,
//            status = false
//        )
//
//        coVerify { insertItemWithCategoryUseCase(expectedItem, "") }
//
//        assertTrue(surveyCompleted)
//
//        verify {
//            scheduleNotificationMock.scheduleNotification(
//                any(), // Context is mocked, so we don't check it here
//                "Test Item".hashCode(),
//                "Test Item",
//                reminderDate,
//                reminderDate
//            )
//        }
//    }
}