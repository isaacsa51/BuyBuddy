package com.serranoie.android.buybuddy.ui.summary.screens

import androidx.compose.runtime.Composable
import com.serranoie.android.buybuddy.data.persistance.entity.ItemPrice

interface ChartProviderIncoming {
    @Composable
    fun GetChart(summaryItemsToBuy: List<ItemPrice>)
}

interface ChartProvider {
    @Composable
    fun GetChart()
}