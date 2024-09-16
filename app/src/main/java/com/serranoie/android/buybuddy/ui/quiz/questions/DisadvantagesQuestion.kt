package com.serranoie.android.buybuddy.ui.quiz.questions

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.common.TextOutlinedField
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.quiz.common.QuestionWrapper
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding

@Composable
fun DisadvantagesQuestion(
    userEventsTracker: UserEventsTracker,
    @StringRes titleResourceId: Int,
    @StringRes directionsResourceId: Int,
    onInputResponse: (String) -> Unit,
    contrasResponse: String,
    modifier: Modifier = Modifier,
) {

    LaunchedEffect(key1 = contrasResponse) {
        if (contrasResponse.isNotBlank()) {
            val inputInfo = mapOf(
                "itemDisadvantages" to contrasResponse,
            )

            userEventsTracker.logQuizInfo("Benefits: ", inputInfo)
        }
    }

    QuestionWrapper(
        titleResourceId = titleResourceId,
        directionsResourceId = directionsResourceId,
        modifier = modifier,
    ) {
        Column {
            TextOutlinedField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = basePadding)
                    .height(120.dp),
                value = contrasResponse,
                onValueChange = onInputResponse,
                label = { Text(text = stringResource(id = R.string.benefits)) },
                isValid = {
                    it.isNotBlank() && it.matches(Regex("^[a-zA-Z0-9\\s]+$"))
                },
                errorMessage = stringResource(id = R.string.field_required),
                maxLines = 5,
                textStyle = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun PopulateDisadvantages(
    userEventsTracker: UserEventsTracker,
    onInputResponse: (String) -> Unit,
    contrasResponse: String,
    modifier: Modifier,
) {
    DisadvantagesQuestion(
        userEventsTracker,
        titleResourceId = R.string.cons_question,
        directionsResourceId = R.string.reasons_helper,
        onInputResponse = onInputResponse,
        contrasResponse = contrasResponse,
        modifier = modifier,
    )
}
