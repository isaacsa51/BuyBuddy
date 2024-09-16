package com.serranoie.android.buybuddy.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.serranoie.android.buybuddy.ui.util.UiConstants.largePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding

@Composable
fun EmptySummary() {
    Column(
        modifier = Modifier
            .padding(basePadding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.52f),
            painter = painterResource(id = R.drawable.analytics),
            contentDescription = "Empty list image",
            contentScale = ContentScale.Fit
        )

        Text(
            text = stringResource(R.string.empty_summary),
            style = MaterialTheme.typography.headlineLarge.copy(
                textAlign = TextAlign.Center
            ),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = largePadding, vertical = smallPadding),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun EmptySummaryPreview() {
    EmptySummary()
}