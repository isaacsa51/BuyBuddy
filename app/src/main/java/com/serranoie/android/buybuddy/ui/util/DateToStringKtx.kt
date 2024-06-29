package com.serranoie.android.buybuddy.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun dateToString(date: Date?): String {
    if (date == null) {
        return "Invalid date"
    }
    val formatter = SimpleDateFormat("dd 'of' MMMM yyyy, hh:mm a", Locale.getDefault())
    return formatter.format(date)
}