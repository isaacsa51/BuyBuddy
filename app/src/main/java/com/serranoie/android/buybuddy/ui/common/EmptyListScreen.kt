package com.serranoie.android.buybuddy.ui.common

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
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
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
            modifier = Modifier.padding(horizontal = basePadding, vertical = smallPadding),
        )

        Text(
            text = stringResource(R.string.empty_items_info),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = basePadding, vertical = smallPadding),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyListScreenPreview() {
    val padding = PaddingValues()

    Surface {
        EmptyListScreen(padding)
    }
}
