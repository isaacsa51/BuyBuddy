package com.serranoie.android.buybuddy.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding

@Composable
fun ErrorLabelTxtFld(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.padding(start = smallPadding)
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorLabelTxtFldPreview() {
    Surface {
        ErrorLabelTxtFld(stringResource(id = R.string.field_required))
    }
}