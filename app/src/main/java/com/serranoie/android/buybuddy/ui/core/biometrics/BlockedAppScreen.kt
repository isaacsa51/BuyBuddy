package com.serranoie.android.buybuddy.ui.core.biometrics

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding

@Composable
fun BlockedAppScreen(onUnlockPressed: () -> Unit) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(basePadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(basePadding)
        ) {
            Image(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.6f),
                imageVector = ImageVector.vectorResource(id = R.drawable.image_authentication),
                contentDescription = "Blocked image",
                contentScale = ContentScale.FillHeight,
            )

            Text(
                text = stringResource(id = R.string.bio_lock_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall
            )

            Text(
                text = stringResource(id = R.string.app_lock_setting_desc),
                style = MaterialTheme.typography.titleLarge.copy(
                    textAlign = TextAlign.Left
                )
            )

            Button(
                onClick = { onUnlockPressed() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = smallPadding)
            ) {
                Text(
                    text = stringResource(id = R.string.unlock_button)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlockedAppScreenPreview() {
    Surface {
        BlockedAppScreen({ })
    }
}