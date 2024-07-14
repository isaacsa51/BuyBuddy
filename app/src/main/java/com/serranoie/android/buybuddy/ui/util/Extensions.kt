package com.serranoie.android.buybuddy.ui.util

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

/**
 * Gets the activity from the context.
 * @return the activity if the context is an instance of [ComponentActivity], null otherwise.
 */
fun Context.getActivity(): ComponentActivity? =
    when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
    }
