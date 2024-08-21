package com.serranoie.android.buybuddy.ui.summary.screens

import androidx.compose.runtime.Composable
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusOne
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusZero
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusOne
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusZero

interface ChartProviderIncoming {
    @Composable
    fun GetChart(summaryItemsToBuy: List<ItemPriceStatusZero>?, yearlySummaryToBuy: List<MonthlySumStatusZero>?)
}

interface ChartProvider {
    @Composable
    fun GetChart(summaryItemsBought: List<ItemPriceStatusOne>?, yearlySummaryBought: List<MonthlySumStatusOne>?)
}