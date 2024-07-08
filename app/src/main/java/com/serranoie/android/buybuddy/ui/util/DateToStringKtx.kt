package com.serranoie.android.buybuddy.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun dateToString(date: Date?): String {
    if (date == null) {
        return "Invalid date"
    }
    val outputFormatter = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault())
    outputFormatter.timeZone = TimeZone.getDefault()
    return try {
        outputFormatter.format(date)
    } catch (e: Exception) {
        "Invalid date format"
    }
}
