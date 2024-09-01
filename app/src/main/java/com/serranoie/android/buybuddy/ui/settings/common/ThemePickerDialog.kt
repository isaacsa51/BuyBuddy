package com.serranoie.android.buybuddy.ui.settings.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.settings.ThemeMode

@Composable
fun ThemePickerDialog(
    themeValue: String,
    showThemeDialog: MutableState<Boolean>,
    onThemeChange: (ThemeMode) -> Unit,
) {
    val context = LocalContext.current
    val themeRadioOptions = listOf(
        stringResource(id = R.string.theme_dialog_option1),
        stringResource(id = R.string.theme_dialog_option2),
        stringResource(id = R.string.theme_dialog_option3)
    )
    val (selectedThemeOption, onThemeOptionSelected) = remember {
        mutableStateOf(themeValue)
    }

    if (showThemeDialog.value) {
        AlertDialog(onDismissRequest = {
            showThemeDialog.value = false
        }, title = {
            Text(
                text = stringResource(id = R.string.theme_dialog_title),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }, text = {
            Column(
                modifier = Modifier.selectableGroup(),
                verticalArrangement = Arrangement.Center,
            ) {
                themeRadioOptions.forEach { text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .selectable(
                                selected = (text == selectedThemeOption),
                                onClick = { onThemeOptionSelected(text) },
                                role = Role.RadioButton,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = (text == selectedThemeOption),
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.inversePrimary,
                                disabledSelectedColor = Color.Black,
                                disabledUnselectedColor = Color.Black
                            ),
                        )
                        Text(
                            text = text,
                            modifier = Modifier.padding(start = 16.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        }, confirmButton = {
            FilledTonalButton(
                onClick = {
                    showThemeDialog.value = false
                    onThemeChange(
                        when (selectedThemeOption) {
                            context.getString(R.string.theme_dialog_option1) -> ThemeMode.Light
                            context.getString(R.string.theme_dialog_option2) -> ThemeMode.Dark
                            else -> ThemeMode.Auto
                        }
                    )
                }, colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(id = R.string.theme_dialog_apply_button))
            }
        }, dismissButton = {
            TextButton(onClick = {
                showThemeDialog.value = false
            }) {
                Text(stringResource(id = R.string.cancel))
            }
        })
    }
}