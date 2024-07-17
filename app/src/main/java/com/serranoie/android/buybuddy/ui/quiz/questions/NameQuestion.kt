package com.serranoie.android.buybuddy.ui.quiz.questions

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.quiz.common.QuestionWrapper
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding

@Composable
fun NameQuestion(
    @StringRes titleResourceId: Int,
    @StringRes directionsResourceId: Int,
    onInputResponse: (String) -> Unit,
    onDescriptionResponse: (String) -> Unit,
    onPriceResponse: (String) -> Unit,
    nameItemResponse: String,
    descriptionResponse: String,
    priceResponse: String,
    modifier: Modifier = Modifier,
) {
    var priceText by remember { mutableStateOf(priceResponse) }
    val isValidPrice = remember { mutableStateOf(true) }

    QuestionWrapper(
        titleResourceId = titleResourceId,
        directionsResourceId = directionsResourceId,
        modifier = modifier,
    ) {
        Column(
            modifier =
            Modifier.padding(
                vertical = basePadding,
            ),
        ) {
            OutlinedTextField(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = smallPadding),
                label = { Text(stringResource(R.string.name)) },
                value = nameItemResponse,
                onValueChange = onInputResponse,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 2,
                textStyle = MaterialTheme.typography.headlineSmall,
            )

            OutlinedTextField(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = smallPadding)
                    .height(120.dp),
                value = descriptionResponse,
                label = { Text(stringResource(R.string.description)) },
                onValueChange = onDescriptionResponse,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 5,
                textStyle = MaterialTheme.typography.bodyLarge,
            )

            OutlinedTextField(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = smallPadding),
                label = { Text(stringResource(R.string.price)) },
                value = priceText,
                onValueChange = { newValue ->
                    priceText = newValue
                    isValidPrice.value = newValue.matches(Regex("^\\d+(\\.\\d+)?$"))
                },
                isError = !isValidPrice.value,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            if (isValidPrice.value && priceText.isNotBlank()) {
                onPriceResponse(priceText)
            }

            if (!isValidPrice.value) {
                Text(
                    text = stringResource(R.string.invalid_price_format),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = smallPadding),
                )
            }
        }
    }
}

@Composable
fun PopulateNameQuestion(
    onInputResponse: (String) -> Unit,
    onDescriptionResponse: (String) -> Unit,
    onPriceResponse: (String) -> Unit,
    priceResponse: String,
    nameItemResponse: String,
    descriptionResponse: String,
    modifier: Modifier,
) {
    NameQuestion(
        titleResourceId = R.string.name_question,
        directionsResourceId = R.string.name_helper,
        onInputResponse = onInputResponse,
        nameItemResponse = nameItemResponse,
        descriptionResponse = descriptionResponse,
        onDescriptionResponse = onDescriptionResponse,
        onPriceResponse = onPriceResponse,
        priceResponse = priceResponse,
        modifier = modifier,
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NameItemQuestionPreview() {
    Surface {
        NameQuestion(
            titleResourceId = R.string.name_question,
            directionsResourceId = R.string.name_helper,
            onInputResponse = { },
            nameItemResponse = "",
            descriptionResponse = "",
            onDescriptionResponse = { },
            onPriceResponse = { },
            priceResponse = "",
        )
    }
}
