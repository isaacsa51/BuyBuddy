package com.serranoie.android.buybuddy.ui.quiz.questions

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.common.TimePickerDialog
import com.serranoie.android.buybuddy.ui.quiz.QuizViewModel
import com.serranoie.android.buybuddy.ui.quiz.common.QuestionWrapper
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderQuestion(
    @StringRes titleResourceId: Int,
    @StringRes directionsResourceId: Int,
    dateInMillis: Date?,
    onDateTimeSelected: (Date) -> Unit,
    modifier: Modifier = Modifier,
) {

    val view = LocalView.current

    QuestionWrapper(
        titleResourceId = titleResourceId,
        directionsResourceId = directionsResourceId,
        modifier = modifier,
    ) {
        val dateFormat =
            SimpleDateFormat(stringResource(R.string.edit_screen_date_format), Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()

        val initialDate = dateInMillis ?: Date()
        var selectedDate by remember { mutableStateOf(initialDate) }

        val dateString = dateFormat.format(selectedDate)

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDate.time,
        )
        var showDatePicker by remember { mutableStateOf(false) }

        val timePickerState = rememberTimePickerState()
        var showTimePicker by remember { mutableStateOf(false) }

        Column {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(basePadding),
                onClick = {
                    view.weakHapticFeedback()
                    showDatePicker = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                ),
            ) {
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            view.weakHapticFeedback()
                            val selectedDateMillis = datePickerState.selectedDateMillis
                            if (selectedDateMillis != null) {
                                val calendar = Calendar.getInstance()
                                calendar.timeInMillis = selectedDateMillis
                                calendar.timeZone = TimeZone.getDefault()
                                selectedDate = calendar.time
                                showDatePicker = false
                                showTimePicker = true
                            }
                        },
                    ) { Text(stringResource(R.string.ok)) }
                },
                dismissButton = {
                    TextButton(onClick = {
                        view.weakHapticFeedback()
                        showDatePicker = false
                    }) { Text(stringResource(id = R.string.cancel)) }
                },
            ) {
                DatePicker(
                    state = datePickerState,
                    dateValidator = { dateInMillis ->
                        dateInMillis >= System.currentTimeMillis()
                    },
                )
            }
        }

        if (showTimePicker) {
            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            view.weakHapticFeedback()
                            val calendar = Calendar.getInstance()
                            calendar.time = selectedDate
                            calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            calendar.set(Calendar.MINUTE, timePickerState.minute)
                            calendar.timeZone = TimeZone.getDefault()
                            selectedDate = calendar.time
                            val selectedCalendar = Calendar.getInstance().apply {
                                timeInMillis = selectedDate.time
                            }
                            if (selectedCalendar.after(Calendar.getInstance())) {
                                onDateTimeSelected(selectedDate)
                                showTimePicker = false
                            } else {
                                selectedDate = initialDate
                            }
                        },
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        view.weakHapticFeedback()
                        showTimePicker = false
                    }) { Text(stringResource(id = R.string.cancel)) }
                },
            ) {
                TimePicker(state = timePickerState)
            }
        }
    }
}

@Composable
fun PopulateReminderQuestion(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
) {
    ReminderQuestion(
        titleResourceId = R.string.reminder_question,
        directionsResourceId = R.string.select_date,
        dateInMillis = viewModel.reminderResponse,
        onDateTimeSelected = { date ->
            viewModel.onReminderResponse(date)
        },
        modifier = modifier,
    )
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ReminderPreview() {
    Surface {
        ReminderQuestion(
            titleResourceId = R.string.reminder_question,
            directionsResourceId = R.string.select_date,
            dateInMillis = Date(),
            onDateTimeSelected = {},
        )
    }
}
