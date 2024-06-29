package com.serranoie.android.buybuddy.ui.quiz.questions

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.serranoie.android.buybuddy.ui.core.theme.BuyBuddyTheme

@Composable
fun DisadvantagesQuestion(
    @StringRes titleResourceId: Int,
    @StringRes directionsResourceId: Int,
    onInputResponse: (String) -> Unit,
    contrasResponse: String,
    modifier: Modifier = Modifier,
) {
    QuestionWrapper(
        titleResourceId = titleResourceId,
        directionsResourceId = directionsResourceId,
        modifier = modifier,
    ) {
        Column {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                label = { Text(stringResource(id = R.string.disadvantages)) },
                value = contrasResponse,
                onValueChange = onInputResponse,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                maxLines = 5,
                textStyle = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun PopulateDisadvantages(
    onInputResponse: (String) -> Unit,
    contrasResponse: String,
    modifier: Modifier,
) {
    DisadvantagesQuestion(
        titleResourceId = R.string.cons_question,
        directionsResourceId = R.string.reasons_helper,
        onInputResponse = onInputResponse,
        contrasResponse = contrasResponse,
        modifier = modifier,
    )
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DisadvantagesPreview() {
    BuyBuddyTheme {
        Surface {
            val onInputResponse by remember { mutableStateOf("") }

            DisadvantagesQuestion(
                titleResourceId = R.string.cons_question,
                directionsResourceId = R.string.reasons_helper,
                onInputResponse = { onInputResponse },
                contrasResponse = "",
            )
        }
    }
}
