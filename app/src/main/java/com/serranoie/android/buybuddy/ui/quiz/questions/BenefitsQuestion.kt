package com.serranoie.android.buybuddy.ui.quiz.questions

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.quiz.common.QuestionWrapper
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding

@Composable
fun BenefitsQuestion(
    @StringRes titleResourceId: Int,
    @StringRes directionsResourceId: Int,
    onInputResponse: (String) -> Unit,
    benefitsResponse: String,
    modifier: Modifier = Modifier,
) {
    QuestionWrapper(
        titleResourceId = titleResourceId,
        directionsResourceId = directionsResourceId,
        modifier = modifier,
    ) {
        Column {
            OutlinedTextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = basePadding)
                        .height(120.dp),
                label = { Text(stringResource(id = R.string.reasons)) },
                value = benefitsResponse,
                onValueChange = onInputResponse,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 5,
                minLines = 3,
                textStyle = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun PopulateBenefitsQuestion(
    onInputResponse: (String) -> Unit,
    benefitsResponse: String,
    modifier: Modifier,
) {
    BenefitsQuestion(
        titleResourceId = R.string.benefit_question,
        directionsResourceId = R.string.reasons_helper,
        onInputResponse = onInputResponse,
        benefitsResponse = benefitsResponse,
        modifier = modifier,
    )
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BenefitsPreview() {
    Surface {
        val onInputResponse by remember { mutableStateOf("") }

        BenefitsQuestion(
            titleResourceId = R.string.benefit_question,
            directionsResourceId = R.string.reasons_helper,
            benefitsResponse = "",
            onInputResponse = { onInputResponse },
        )
    }
}
