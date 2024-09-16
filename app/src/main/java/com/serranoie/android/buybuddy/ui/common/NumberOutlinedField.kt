package com.serranoie.android.buybuddy.ui.common

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NumberOutlinedField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isValid: (String) -> Boolean,
    errorMessage: String,
    prefix: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
        prefix = prefix,
        keyboardOptions = keyboardOptions,
        maxLines = maxLines,
        textStyle = textStyle,
        shape = shape
    )

    if (isInvalid) {
        ErrorLabelTxtFld(text = errorMessage)
    }
}