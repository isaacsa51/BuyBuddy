package com.serranoie.android.buybuddy.ui.quiz.questions

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.quiz.QuizViewModel
import com.serranoie.android.buybuddy.ui.quiz.common.QuestionWrapper
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.strongHapticFeedback
import kotlin.math.roundToInt

@Composable
fun UsageQuestion(
    userEventsTracker: UserEventsTracker,
    @StringRes titleResourceId: Int,
    value: Int,
    onValueChange: (Int) -> Unit,
    @StringRes startTextResource: Int,
    @StringRes neutralTextResource: Int,
    @StringRes endTextResource: Int,
    modifier: Modifier = Modifier,
) {
    val steps =
        listOf(
            R.string.usage_barely,
            R.string.usage_rarely,
            R.string.usage_ocasionally,
            R.string.usage_sometimes,
            R.string.usage_often,
            R.string.usage_almost_everyday,
        )

    val view = LocalView.current
    val sliderRange = 0f..(steps.size - 1).toFloat()
    var sliderPosition by remember { mutableFloatStateOf(value.toFloat()) }
    val selectedIndex = sliderPosition.roundToInt()

    QuestionWrapper(
        titleResourceId = titleResourceId,
        modifier = modifier,
    ) {
        Row {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(basePadding),
            ) {
                Text(
                    text = stringResource(steps[selectedIndex]),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = basePadding),
                )

                Slider(
                    value = sliderPosition,
                    valueRange = sliderRange,
                    steps = steps.size - 2,
                    onValueChange = { newValue ->
                        sliderPosition = newValue
                        onValueChange(steps[newValue.roundToInt()])
                        view.strongHapticFeedback()
                    },
                    onValueChangeFinished = {
                        userEventsTracker.logQuizInfo(
                            "Usage: ",
                            mapOf("Item usage: " to steps[selectedIndex].toString())
                        )
                    }
                )
            }
        }
        Row {
            Text(
                text = stringResource(id = startTextResource),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1.8f),
            )
            Text(
                text = stringResource(id = neutralTextResource),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1.8f),
            )
            Text(
                text = stringResource(id = endTextResource),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1.8f),
            )
        }
    }
}

@Composable
fun PopulateUsageQuestion(
    userEventsTracker: UserEventsTracker,
    viewModel: QuizViewModel,
    value: String?,
    onValueChange: (Int) -> Unit,
    modifier: Modifier,
) {
    val context = LocalContext.current

    val usageIndex =
        listOf(
            R.string.usage_barely,
            R.string.usage_rarely,
            R.string.usage_ocasionally,
            R.string.usage_sometimes,
            R.string.usage_often,
            R.string.usage_almost_everyday,
        ).indexOfFirst { context.getString(it) == value }

    UsageQuestion(
        userEventsTracker,
        titleResourceId = R.string.usage_question,
        value = if (usageIndex != -1) usageIndex else 0,
        onValueChange = viewModel::onUsageResponse,
        startTextResource = R.string.usage_barely,
        neutralTextResource = R.string.usage_sometimes,
        endTextResource = R.string.usage_almost_everyday,
        modifier = modifier,
    )
}
