package com.serranoie.android.buybuddy.ui.quiz

import android.app.Application
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.domain.model.Item
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import com.serranoie.android.buybuddy.domain.usecase.item.InsertItemWithCategoryUseCase
import com.serranoie.android.buybuddy.ui.core.ScheduleNotification
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.quiz.common.Questions
import com.serranoie.android.buybuddy.ui.quiz.questions.Category
import com.serranoie.android.buybuddy.ui.util.toToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val insertItemWithCategoryUseCase: InsertItemWithCategoryUseCase,
    application: Application,
    private val userEventsTracker: UserEventsTracker,
) : AndroidViewModel(application) {

    private val scheduleNotification by lazy { ScheduleNotification() }

    private val questionOrder: List<Questions> = listOf(
        Questions.NAME,
        Questions.CATEGORY,
        Questions.USAGE,
        Questions.BENEFITS,
        Questions.CONTRAS,
        Questions.REMINDER,
    )

    private var questionIndex = 0

    private val _isNextEnabled = mutableStateOf(false)
    val isNextEnabled: Boolean
        get() = _isNextEnabled.value

    // Responses States
    private val _nameItemResponse = mutableStateOf("")
    val nameItemResponse: String
        get() = _nameItemResponse.value

    private val _descriptionItemResponse = mutableStateOf("")
    val descriptionItemResponse: String
        get() = _descriptionItemResponse.value

    private val _priceResponse = mutableStateOf("")
    val priceResponse: String
        get() = _priceResponse.value

    private val _categoryResponse = mutableStateOf<Category?>(null)
    val categoryResponse: Category?
        get() = _categoryResponse.value

    var selectedCategoryName by mutableStateOf("")

    private val _benefitsResponse = mutableStateOf("")
    val benefitsResponse: String
        get() = _benefitsResponse.value

    private val _contrasResponse = mutableStateOf("")
    val contrasResponse: String
        get() = _contrasResponse.value

    private val _usageResponse = mutableStateOf("")
    val usageResponse: String
        get() = _usageResponse.value

    var usage by mutableStateOf("")

    private val _reminderResponse = mutableStateOf<Date?>(null)
    val reminderResponse: Date?
        get() = _reminderResponse.value

    private val _quizScreenData = mutableStateOf(createQuizScreenData())
    val quizScreenData: QuizScreenData
        get() = _quizScreenData.value

    // Quiz status exposed as state
    fun onNameResponse(itemName: String) {
        _nameItemResponse.value = itemName
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onDescriptionResponse(description: String) {
        _descriptionItemResponse.value = description
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onPriceResponse(price: String) {
        _priceResponse.value = price
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onCategoryResponse(category: Category) {
        _categoryResponse.value = category
        selectedCategoryName = getApplication<Application>().getString(category.name)
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onUsageResponse(@StringRes usageResId: Int) {
        _usageResponse.value = getApplication<Application>().getString(usageResId)
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onBenefitsResponse(reasonsTo: String) {
        _benefitsResponse.value = reasonsTo
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onContrasResponse(reasonsNotTo: String) {
        _contrasResponse.value = reasonsNotTo
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onReminderResponse(date: Date) {
        _reminderResponse.value = date
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onBackPressed(): Boolean {
        if (questionIndex == 0) {
            return false
        }
        changeQuestion(questionIndex - 1)
        return true
    }

    fun onPreviousPressed() {
        if (questionIndex == 0) {
            throw IllegalStateException("onPreviousPressed when on question 0")
        }
        changeQuestion(questionIndex - 1)
    }

    fun onNextPressed() {
        changeQuestion(questionIndex + 1)
    }

    private fun changeQuestion(newQuestionIndex: Int) {
        questionIndex = newQuestionIndex
        _isNextEnabled.value = getIsNextEnabled()
        _quizScreenData.value = createQuizScreenData()
    }

    fun onDonePressed(
        onSurveyComplete: () -> Unit,
    ) {
        val itemData = Item(
            itemId = null,
            name = nameItemResponse,
            categoryId = categoryResponse?.categoryId ?: 0,
            description = descriptionItemResponse,
            usage = usageResponse,
            benefits = benefitsResponse,
            disadvantages = contrasResponse,
            reminderDate = reminderResponse,
            reminderTime = reminderResponse,
            price = if (priceResponse.isNotEmpty()) priceResponse.toDouble() else 0.0,
            status = false,
        )

        viewModelScope.launch {
            insertItemWithCategoryUseCase(itemData, selectedCategoryName).collect { result ->

                when (result) {
                    is UseCaseResult.Success<*> -> {
                        val itemId = result.data

                        reminderResponse?.let {
                            scheduleNotification.scheduleNotification(
                                context = getApplication<Application>().applicationContext,
                                itemId = itemId.toString().toInt(),
                                itemName = itemData.name,
                                reminderDate = reminderResponse,
                                reminderTime = reminderResponse
                            )
                        }
                        onSurveyComplete()
                    }

                    is UseCaseResult.Error -> {
                        result.exception.printStackTrace()
                        userEventsTracker.logException(result.exception)
                        val context = getApplication<Application>().applicationContext
                        context.getString(R.string.error_saving_data).toToast(context)
                    }
                }
            }
        }
    }

    private fun getIsNextEnabled(): Boolean {
        return when (questionOrder[questionIndex]) {
            Questions.NAME -> _nameItemResponse.value.isNotEmpty() && _descriptionItemResponse.value.isNotEmpty() && _priceResponse.value.isNotEmpty()

            Questions.CATEGORY -> _categoryResponse.value != null
            Questions.USAGE -> _usageResponse.value != null
            Questions.BENEFITS -> _benefitsResponse.value.isNotEmpty()
            Questions.CONTRAS -> _contrasResponse.value.isNotEmpty()
            Questions.REMINDER -> reminderResponse != null
        }
    }

    private fun createQuizScreenData(): QuizScreenData {
        return QuizScreenData(
            questionIndex = questionIndex,
            questionCount = questionOrder.size,
            shouldShowPreviousButton = questionIndex > 0,
            shouldShowDoneButton = questionIndex == questionOrder.size - 1,
            currentQuestion = questionOrder[questionIndex],
        )
    }
}