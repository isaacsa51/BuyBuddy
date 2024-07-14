package com.serranoie.android.buybuddy.ui.onboard

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.mediumPadding

@Composable
fun OnBoardingPage(page: Page) {
    Column(modifier = Modifier) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.5f),
            painter = painterResource(id = page.image),
            contentDescription = stringResource(R.string.onboarding_image_label),
            contentScale = ContentScale.Fit,
        )

        Text(
            text = stringResource(id = page.title),
            modifier = Modifier.padding(horizontal = mediumPadding),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
        )
        Text(
            text = stringResource(id = page.description),
            modifier = Modifier.padding(horizontal = mediumPadding, vertical = basePadding),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewOnBoardingPage() {
    Surface {
        OnBoardingPage(page = pages[0])
    }
}
