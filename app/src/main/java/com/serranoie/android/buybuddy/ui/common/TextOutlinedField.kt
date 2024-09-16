package com.serranoie.android.buybuddy.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R

@Composable
fun TextOutlinedField(
    value: String,
    onValueChange: (String) -> Unit, label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isValid: (String) -> Boolean,
    errorMessage: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = Int.MAX_VALUE,
    textStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = MaterialTheme.shapes.small,
) {
    var isInvalid by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            isInvalid = !isValid(it)
        },
        label = label,
        modifier = modifier,
        isError = isInvalid,
        keyboardOptions = keyboardOptions,
        maxLines = maxLines,
        textStyle = textStyle,
        shape = shape
    )

    if (isInvalid) {
        ErrorLabelTxtFld(text = errorMessage)
    }
}

@Preview(showBackground = true)
@Composable
fun TextOutlinedTextFieldPreview() {
    TextOutlinedField(
        value = "itemName",
        onValueChange = { },
        label = { Text(stringResource(id = R.string.name)) },
        modifier = Modifier.fillMaxWidth(),
        isValid = { it.isNotBlank() && it.matches(Regex("^[a-zA-Z0-9\\s]+$")) },
        errorMessage = stringResource(id = R.string.auth_error),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        maxLines = 2,
        textStyle = MaterialTheme.typography.titleLarge,
        shape = RoundedCornerShape(7.dp)
    )
}