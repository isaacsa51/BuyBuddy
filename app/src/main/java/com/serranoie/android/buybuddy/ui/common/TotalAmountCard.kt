package com.serranoie.android.buybuddy.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.core.theme.BuyBuddyTheme
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.extraSmallPadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.mediumPadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding

@Composable
fun TotalAmountCard(totalPrice: Double, totalBoughtPrice: Double) {
    Row(
        modifier = Modifier
            .padding(horizontal = extraSmallPadding, vertical = smallPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(extraSmallPadding)
    ) {
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseSurface,
            ),
        ) {
            Column(
                modifier = Modifier.padding(vertical = mediumPadding, horizontal = basePadding)
            ) {
                Text(
                    text = stringResource(R.string.total_spent),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )

                Text(
                    text = String.format("$ %.2f", totalBoughtPrice),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        OutlinedCard(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.padding(vertical = mediumPadding, horizontal = basePadding)
            ) {
                Text(
                    text = stringResource(R.string.expected_expenses),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall,
                )

                Text(
                    text = String.format("$ %.2f", totalPrice),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TotalAmountCardPreview() {
    BuyBuddyTheme {
        Surface {
            TotalAmountCard(251.45, 0.0)
        }
    }
}