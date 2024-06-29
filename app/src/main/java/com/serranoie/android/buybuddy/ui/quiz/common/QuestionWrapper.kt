package com.serranoie.android.buybuddy.ui.quiz.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.core.theme.BuyBuddyTheme

/**
 * A scrollable container with the question's title, direction, and dynamic content.
 *
 * @param titleResourceId String resource to use for the question's title
 * @param modifier Modifier to apply to the entire wrapper
 * @param directionsResourceId String resource to use for the question's directions; the direction
 * UI will be omitted if null is passed
 * @param content Composable to display after the title and option directions
 */
@Composable
fun QuestionWrapper(
    @StringRes titleResourceId: Int,
    modifier: Modifier = Modifier,
    @StringRes directionsResourceId: Int? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.height(32.dp))
        QuestionTitle(titleResourceId)
        directionsResourceId?.let {
            Spacer(Modifier.height(18.dp))
            QuestionDirections(it)
        }
        content()
    }
}

@Composable
private fun QuestionTitle(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = title),
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.inverseSurface,
                shape = MaterialTheme.shapes.small,
            )
            .padding(vertical = 24.dp, horizontal = 16.dp),
    )
}

@Composable
private fun QuestionDirections(
    @StringRes directionsResourceId: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = directionsResourceId),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@PreviewLightDark
@Composable
private fun QuestionWrapperPreview() {
    BuyBuddyTheme {
        Surface {
            Column {
                QuestionTitle(title = R.string.name_question)
                QuestionDirections(directionsResourceId = R.string.name_helper)
            }
        }
    }
}
