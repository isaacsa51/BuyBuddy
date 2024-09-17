package com.serranoie.android.buybuddy.ui.quiz.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.quiz.questions.Category
import com.serranoie.android.buybuddy.ui.util.UiConstants.extraSmallPadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding

@Composable
fun RadioButtonOption(
    text: String,
    selected: Boolean,
    onOptionSelected: (Category) -> Unit,
    modifier: Modifier = Modifier,
    category: Category,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.outline
            },
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .selectable(
                selected,
                onClick = { onOptionSelected(category) },
                role = Role.RadioButton,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(extraSmallPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(Modifier.width(smallPadding))

            Text(text, Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)

            Box(Modifier.padding(extraSmallPadding - 2.dp)) {
                RadioButton(selected, onClick = { onOptionSelected(category) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioButtonOptionPreview() {
    RadioButtonOption(
        text = "Gaming",
        selected = true,
        onOptionSelected = { },
        category = Category(1, R.string.category_books)
    )
}