package com.serranoie.android.buybuddy.ui.summary.screens

import androidx.compose.runtime.Composable
import com.serranoie.android.buybuddy.domain.model.ItemPrice
import com.serranoie.android.buybuddy.domain.model.MonthlySum

interface ChartProviderIncoming {
    @Composable
    fun GetChart(summaryItemsToBuy: List<ItemPrice>?, yearlySummaryToBuy: List<MonthlySum>?)
}

interface ChartProvider {
    @Composable
    fun GetChart(summaryItemsBought: List<ItemPrice>?, yearlySummaryBought: List<MonthlySum>?)
}