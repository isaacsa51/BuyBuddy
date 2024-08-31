package com.serranoie.android.buybuddy.ui.quiz

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.serranoie.android.buybuddy.ui.quiz.common.QuizBottomBar
import com.serranoie.android.buybuddy.ui.quiz.common.QuizTopBar

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
