package com.serranoie.android.buybuddy.ui.summary

import androidx.compose.runtime.Composable

data class SummaryItem(
    val label : String,
    val screen: @Composable () -> Unit,
)
