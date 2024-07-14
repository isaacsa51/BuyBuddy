package com.serranoie.android.buybuddy.ui.quiz.common

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.core.theme.stronglyDeemphasizedAlpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizTopBar(
    questionIndex: Int,
    totalQuestionsCount: Int,
    onClosePressed: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CenterAlignedTopAppBar(
            title = {
                TopAppBarTitle(
                    questionIndex = questionIndex,
                    totalQuestionsCount = totalQuestionsCount,
                )
            },
            actions = {
                IconButton(
                    onClick = onClosePressed,
                    modifier = Modifier.padding(4.dp),
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close),
                        tint = MaterialTheme.colorScheme.onSurface.copy(stronglyDeemphasizedAlpha),
                    )
                }
            },
        )

        val animatedProgress by animateFloatAsState(
            targetValue = (questionIndex + 1) / totalQuestionsCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.tertiary,
            trackColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.13f),
        )
    }
}

@Composable
private fun TopAppBarTitle(
    questionIndex: Int,
    totalQuestionsCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = (questionIndex + 1).toString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(R.string.quiz_topbar_count, totalQuestionsCount),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        )
    }
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SurveyTopBarPreview() {
    Surface {
        QuizTopBar(
            questionIndex = 3,
            totalQuestionsCount = 6,
            onClosePressed = { },
        )
    }
}
