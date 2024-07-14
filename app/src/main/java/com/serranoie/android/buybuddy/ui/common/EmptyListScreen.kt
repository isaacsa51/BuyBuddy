package com.serranoie.android.buybuddy.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding

@Composable
fun EmptyListScreen(padding: PaddingValues) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.52f),
            painter = painterResource(id = R.drawable.image_empty),
            contentDescription = "Empty list image",
            contentScale = ContentScale.Fit,
        )

        Text(
            text = stringResource(R.string.empty_items_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = smallPadding),
        )

        Text(
            text = stringResource(R.string.empty_items_info),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = smallPadding),
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = false,
)
@Composable
private fun EmptyListScreenPreview() {
    val padding = PaddingValues()

    Surface {
        EmptyListScreen(padding)
    }
}
