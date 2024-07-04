package com.serranoie.android.buybuddy.ui.quiz

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.core.theme.BuyBuddyTheme
import com.serranoie.android.buybuddy.ui.quiz.common.Questions
import com.serranoie.android.buybuddy.ui.quiz.common.QuizBottomBar
import com.serranoie.android.buybuddy.ui.quiz.common.QuizTopBar
import com.serranoie.android.buybuddy.ui.quiz.questions.NameQuestion

@Composable
fun QuizContentScreen(
    quizScreenData: QuizScreenData,
    isNextEnabled: Boolean,
    onClosePressed: () -> Unit,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Surface {
        Scaffold(
            topBar = {
                QuizTopBar(
                    questionIndex = quizScreenData.questionIndex,
                    totalQuestionsCount = quizScreenData.questionCount,
                    onClosePressed = onClosePressed,
                )
            },
            content = content,
            bottomBar = {
                QuizBottomBar(
                    shouldShowPreviousButton = quizScreenData.shouldShowPreviousButton,
                    shouldShowDoneButton = quizScreenData.shouldShowDoneButton,
                    isNextButtonEnabled = isNextEnabled,
                    onPreviousPressed = onPreviousPressed,
                    onNextPressed = onNextPressed,
                    onDonePressed = onDonePressed,
                )
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun QuizContentScreenPreview() {
    BuyBuddyTheme {
        Surface {
            QuizContentScreen(
                quizScreenData =
                    QuizScreenData(
                        questionIndex = 1,
                        questionCount = 5,
                        shouldShowPreviousButton = true,
                        shouldShowDoneButton = true,
                        currentQuestion = Questions.USAGE,
                    ),
                isNextEnabled = true,
                onPreviousPressed = { },
                onNextPressed = { },
                onDonePressed = { },
                onClosePressed = { },
                content = { paddingValues ->
                    NameQuestion(
                        titleResourceId = R.string.name_question,
                        directionsResourceId = R.string.name_helper,
                        onInputResponse = { },
                        nameItemResponse = "Sample Name",
                        descriptionResponse = "Sample Description",
                        onDescriptionResponse = { },
                        onPriceResponse = { },
                        priceResponse = "10.00",
                    )
                },
            )
        }
    }
}
