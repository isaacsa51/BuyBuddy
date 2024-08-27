package com.serranoie.android.buybuddy.ui.settings.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding

@Composable
fun SettingsContainer(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.padding(smallPadding),
        colors = CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surfaceColorAtElevation(
                3.dp,
            ),
        ),
    ) {
        Column(modifier = Modifier.padding(top = 2.dp)) {
            content()
        }
    }
}