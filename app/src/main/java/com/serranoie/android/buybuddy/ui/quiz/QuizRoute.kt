package com.serranoie.android.buybuddy.ui.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.quiz.common.Questions
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateBenefitsQuestion
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateCategoryQuestion
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateDisadvantages
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateNameQuestion
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateReminderQuestion
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateUsageQuestion
import com.serranoie.android.buybuddy.ui.util.UiConstants.CONTENT_ANIMATION_DURATION
import com.serranoie.android.buybuddy.ui.util.strongHapticFeedback
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback

@Composable
fun QuizRoute(
    userEventsTracker: UserEventsTracker,
    onQuizComplete: () -> Unit,
    onNavUp: () -> Unit,
) {
    val view = LocalView.current
    val viewModel: QuizViewModel = hiltViewModel()
    val quizScreenData = viewModel.quizScreenData
    val isNextEnabled = viewModel.isNextEnabled

    QuizContentScreen(quizScreenData = quizScreenData,
        isNextEnabled = isNextEnabled,
        onPreviousPressed = {
            userEventsTracker.logButtonAction("quiz_prev_button")
            viewModel.onPreviousPressed()
            view.weakHapticFeedback()
        },
        onNextPressed = {
            userEventsTracker.logButtonAction("quiz_next_button")
            viewModel.onNextPressed()
            view.weakHapticFeedback()
        },
        onClosePressed = {
            userEventsTracker.logButtonAction("quiz_close_button")
            view.strongHapticFeedback()
            onNavUp()
        },
        onDonePressed = {
            userEventsTracker.logButtonAction("quiz_done_button")
            viewModel.onDonePressed(
                onQuizComplete
            )
            view.strongHapticFeedback()
        }) { paddingValues ->

        val modifier = Modifier
            .padding(paddingValues)
            .statusBarsPadding()

        BackHandler {
            if (!viewModel.onBackPressed()) {
                onNavUp()
            }
        }

        AnimatedContent(
            targetState = quizScreenData, transitionSpec = {
                val animationSpec: TweenSpec<IntOffset> = tween(CONTENT_ANIMATION_DURATION)
                val direction = getTransitionDirection(
                    initialIndex = initialState.questionIndex,
                    targetIndex = targetState.questionIndex,
                )
                slideIntoContainer(
                    towards = direction,
                    animationSpec = animationSpec,
                ) togetherWith slideOutOfContainer(
                    towards = direction,
                    animationSpec = animationSpec,
                )
            }, label = "surveyScreenDataAnimation"
        ) { targetState ->
            when (targetState.currentQuestion) {
                Questions.NAME -> PopulateNameQuestion(
                    userEventsTracker,
                    nameItemResponse = viewModel.nameItemResponse,
                    onInputResponse = viewModel::onNameResponse,
                    descriptionResponse = viewModel.descriptionItemResponse,
                    onDescriptionResponse = viewModel::onDescriptionResponse,
                    priceResponse = viewModel.priceResponse,
                    onPriceResponse = viewModel::onPriceResponse,
                    modifier = modifier
                )

                Questions.CATEGORY -> PopulateCategoryQuestion(
                    userEventsTracker,
                    selectedAnswer = viewModel.selectedCategoryName,
                    onOptionSelected = viewModel::onCategoryResponse,
                    modifier = modifier
                )

                Questions.BENEFITS -> PopulateBenefitsQuestion(
                    userEventsTracker,
                    benefitsResponse = viewModel.benefitsResponse,
                    onInputResponse = viewModel::onBenefitsResponse,
                    modifier = modifier
                )

                Questions.CONTRAS -> PopulateDisadvantages(
                    userEventsTracker,
                    onInputResponse = viewModel::onContrasResponse,
                    contrasResponse = viewModel.contrasResponse,
                    modifier = modifier,
                )

                Questions.USAGE -> PopulateUsageQuestion(
                    userEventsTracker,
                    value = viewModel.usage,
                    onValueChange = viewModel::onUsageResponse,
                    modifier = modifier,
                    viewModel = viewModel
                )

                Questions.REMINDER -> PopulateReminderQuestion(
                    userEventsTracker,
                    viewModel = viewModel,
                    modifier = modifier,
                )
            }
        }
    }
}

private fun getTransitionDirection(
    initialIndex: Int,
    targetIndex: Int,
): AnimatedContentTransitionScope.SlideDirection {
    return if (targetIndex > initialIndex) {
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        AnimatedContentTransitionScope.SlideDirection.Right
    }
}