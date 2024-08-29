package com.serranoie.android.buybuddy.ui.util

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Toast
import androidx.compose.ui.unit.dp

object UiConstants {
    val extraSmallPadding = 6.dp
    val smallPadding = 8.dp
    val basePadding = 16.dp
    val mediumPadding = 24.dp
    val largePadding = 36.dp

    val indicatorSize = 14.dp
    val pageIndicatorWidth = 52.dp

    const val CONTENT_ANIMATION_DURATION = 300
}

fun View.weakHapticFeedback() {
    this.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
}

fun View.strongHapticFeedback() {
    this.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
}

fun String.
        toToast(context: Context, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, length).show()
}