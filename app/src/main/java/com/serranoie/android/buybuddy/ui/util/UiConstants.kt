package com.serranoie.android.buybuddy.ui.util

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp

object UiConstants {
    val extraSmallPadding = 6.dp
    val smallPadding = 8.dp
    val basePadding = 16.dp
    val mediumPadding = 24.dp
    val largePadding = 36.dp

    val commonCornerRadius = 5.dp

    val indicatorSize = 14.dp
    val pageIndicatorWidth = 52.dp

    val sliderHeight = 56.dp

    val timePickerHeight = 40.dp

    val modalDrawerWidth = 295.dp
    val modalDrawerShape = 14.dp
    val drawerHeaderHeight = 140.dp
    val drawerAppSpacing = 20.dp
    val drawerIconSize = 60.dp
    val drawerItemHeight = 48.dp


    const val CONTENT_ANIMATION_DURATION = 300

    const val BACKUP_FILE_NAME = "buybuddy_backup.json"
}

fun View.weakHapticFeedback() {
    this.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
}

fun View.strongHapticFeedback() {
    this.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
}

fun String.toToast(context: Context, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, this, length).show()
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}