package com.serranoie.android.buybuddy.ui.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.strongHapticFeedback
import kotlinx.coroutines.launch

@Composable
fun QuizFinishedScreen(onDonePressed: () -> Unit) {
    val view = LocalView.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        content = { innerPadding ->
            val modifier = Modifier.padding(innerPadding)
            QuizResult(
                title = stringResource(R.string.finished_quiz_title),
                subtitle = stringResource(R.string.finished_quiz_subtitle),
                description = stringResource(R.string.finished_quiz_info),
                modifier = modifier,
            )
        },
        bottomBar = {
            Surface(
                modifier =
                    Modifier
                        .navigationBarsPadding()
                        .fillMaxWidth(),
                color = Color.Transparent,
            ) {
                Button(
                    onClick = {
                        view.strongHapticFeedback()
                        scope.launch {
                            snackbarHostState.showSnackbar(context.getString(R.string.reminder_saved))
                            onDonePressed()
                        }
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(basePadding),
                ) {
                    Text(text = stringResource(id = R.string.done))
                }
            }
        },
    )
}

@Composable
private fun QuizResult(
    title: String,
    subtitle: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(fraction = 0.7f),
                    painter = painterResource(id = R.drawable.image_notification),
                    contentDescription = "Quiz completed",
                    contentScale = ContentScale.Fit,
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = basePadding, vertical = basePadding),
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = basePadding),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewSurveyResult() {
    Surface {
        QuizFinishedScreen(
            onDonePressed = {},
        )
    }
}
