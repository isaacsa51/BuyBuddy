package com.serranoie.android.buybuddy.ui.quiz.questions

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.common.TextOutlinedField
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.quiz.common.QuestionWrapper
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding

@Composable
fun BenefitsQuestion(
    userEventsTracker: UserEventsTracker,
    @StringRes titleResourceId: Int,
    @StringRes directionsResourceId: Int,
    onInputResponse: (String) -> Unit,
    benefitsResponse: String,
    modifier: Modifier = Modifier,
) {

    LaunchedEffect(key1 = benefitsResponse) {
        if (benefitsResponse.isNotBlank()) {
            val inputInfo = mapOf(
                "itemBenefits" to benefitsResponse,
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
                value = benefitsResponse,
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
fun PopulateBenefitsQuestion(
    userEventsTracker: UserEventsTracker,
    onInputResponse: (String) -> Unit,
    benefitsResponse: String,
    modifier: Modifier,
) {
    BenefitsQuestion(
        userEventsTracker,
        titleResourceId = R.string.benefit_question,
        directionsResourceId = R.string.reasons_helper,
        onInputResponse = onInputResponse,
        benefitsResponse = benefitsResponse,
        modifier = modifier,
    )
}
