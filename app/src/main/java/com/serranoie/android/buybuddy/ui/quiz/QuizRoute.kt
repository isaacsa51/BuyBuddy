package com.serranoie.android.buybuddy.ui.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import com.serranoie.android.buybuddy.ui.quiz.common.Questions
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateBenefitsQuestion
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateCategoryQuestion
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateDisadvantages
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateNameQuestion
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateReminderQuestion
import com.serranoie.android.buybuddy.ui.quiz.questions.PopulateUsageQuestion
import com.serranoie.android.buybuddy.ui.util.UiConstants.CONTENT_ANIMATION_DURATION

@Composable
fun QuizRoute(
    onQuizComplete: () -> Unit,
    onNavUp: () -> Unit,
) {
    val viewModel: QuizViewModel = hiltViewModel()
    val quizScreenData = viewModel.quizScreenData
    val isNextEnabled = viewModel.isNextEnabled

    QuizContentScreen(quizScreenData = quizScreenData,
        isNextEnabled = isNextEnabled,
        onPreviousPressed = { viewModel.onPreviousPressed() },
        onNextPressed = { viewModel.onNextPressed() },
        onClosePressed = {
            onNavUp()
        },
        onDonePressed = {
            viewModel.onDonePressed(
                onQuizComplete
            )
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
                    nameItemResponse = viewModel.nameItemResponse,
                    onInputResponse = viewModel::onNameResponse,
                    descriptionResponse = viewModel.descriptionItemResponse,
                    onDescriptionResponse = viewModel::onDescriptionResponse,
                    priceResponse = viewModel.priceResponse,
                    onPriceResponse = viewModel::onPriceResponse,
                    modifier = modifier
                )

                Questions.CATEGORY -> PopulateCategoryQuestion(
                    selectedAnswer = viewModel.selectedCategoryName,
                    onOptionSelected = viewModel::onCategoryResponse,
                    modifier = modifier
                )

                Questions.BENEFITS -> PopulateBenefitsQuestion(
                    benefitsResponse = viewModel.benefitsResponse,
                    onInputResponse = viewModel::onBenefitsResponse,
                    modifier = modifier
                )

                Questions.CONTRAS -> PopulateDisadvantages(
                    onInputResponse = viewModel::onContrasResponse,
                    contrasResponse = viewModel.contrasResponse,
                    modifier = modifier,
                )

                Questions.USAGE -> PopulateUsageQuestion(
                    value = viewModel.usage,
                    onValueChange = viewModel::onUsageResponse,
                    modifier = modifier,
                    viewModel = viewModel
                )

                Questions.REMINDER -> PopulateReminderQuestion(
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
        // Going forwards in the survey: Set the initial offset to start
        // at the size of the content so it slides in from right to left, and
        // slides out from the left of the screen to -fullWidth
        AnimatedContentTransitionScope.SlideDirection.Left
    } else {
        // Going back to the previous question in the set, we do the same
        // transition as above, but with different offsets - the inverse of
        // above, negative fullWidth to enter, and fullWidth to exit.
        AnimatedContentTransitionScope.SlideDirection.Right
    }
}