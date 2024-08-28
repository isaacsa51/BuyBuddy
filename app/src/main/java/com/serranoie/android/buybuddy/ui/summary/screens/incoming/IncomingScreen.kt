package com.serranoie.android.buybuddy.ui.summary.screens.incoming

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusZero
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusZero
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusZero
import com.serranoie.android.buybuddy.ui.common.EmptySummary
import com.serranoie.android.buybuddy.ui.summary.screens.ChartProviderIncoming
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.mediumPadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import com.serranoie.android.buybuddy.ui.util.Utils.formatPrice
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import kotlin.math.absoluteValue

val monthSummaryChart = object : ChartProviderIncoming {
    @Composable
    override fun GetChart(
        summaryItemsToBuy: List<ItemPriceStatusZero>?,
        yearlySummaryToBuy: List<MonthlySumStatusZero>?
    ) {
        LineChartMonthSummary(summaryItemsToBuy)
    }
}

val yearSummaryChart = object : ChartProviderIncoming {
    @Composable
    override fun GetChart(
        summaryItemsToBuy: List<ItemPriceStatusZero>?,
        yearlySummaryToBuy: List<MonthlySumStatusZero>?
    ) {
        BarsChartYearlySummary(yearlySummaryToBuy)
    }
}

@Composable
fun IncomingScreen(
    summaryItemsToBuy: List<ItemPriceStatusZero>?,
    yearlySummaryToBuy: List<MonthlySumStatusZero>?,
    monthlyCategorySumToBuy: List<MonthlySumCategoryStatusZero>?
) {
    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
        ) {
            item { HeaderInformation(summaryItemsToBuy, yearlySummaryToBuy) }

            if (monthlyCategorySumToBuy?.isNotEmpty() == true) {
                item { CategoryListSummary(monthlyCategorySumToBuy) }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HeaderInformation(
    summaryItemsToBuy: List<ItemPriceStatusZero>?, yearlySummaryToBuy: List<MonthlySumStatusZero>?
) {

    if (summaryItemsToBuy?.isEmpty() == true || yearlySummaryToBuy?.isEmpty() == true) {
        EmptySummary()
    } else {
        val charts = listOf(
            monthSummaryChart, yearSummaryChart
        )

        val pagerState = rememberPagerState(pageCount = {
            charts.size
        })

        var totalToBuy = 0.0
        summaryItemsToBuy?.forEach { item ->
            totalToBuy += item.price ?: 0.0
        }

        Column(
            modifier = Modifier
                .padding(vertical = smallPadding, horizontal = basePadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.total_title_incoming), style = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = TextAlign.Center
                ),
            )

            Text(
                text = "$ " + formatPrice(totalToBuy),
                style = MaterialTheme.typography.displayMedium
            )
        }

        Column(modifier = Modifier.padding(smallPadding)) {

            if (summaryItemsToBuy?.size!! <= 1) {
                Card(
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor =
                        MaterialTheme.colorScheme.surfaceColorAtElevation(
                            3.dp,
                        ),
                    )
                ) {
                    BarsChartYearlySummary(yearlySummaryToBuy)
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(end = 38.dp),
                    pageSize = PageSize.Fill
                ) { page ->
                    Card(
                        modifier = Modifier
                            .height(250.dp)
                            .padding(start = 0.dp, end = basePadding)
                            .graphicsLayer {
                                val pageOffset =
                                    ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            },
                        colors = CardDefaults.cardColors(
                            containerColor =
                            MaterialTheme.colorScheme.surfaceColorAtElevation(
                                3.dp,
                            ),
                        )
                    ) {
                        charts[page].GetChart(summaryItemsToBuy, yearlySummaryToBuy)
                    }
                }
            }
        }
    }
}

@Composable
private fun LineChartMonthSummary(summaryItemsToBuy: List<ItemPriceStatusZero>?) {

    val dataOfProducts: List<Double>? = summaryItemsToBuy?.map { it.price ?: 0.0 }

    Column(
        Modifier.fillMaxSize()
    ) {
        LineChart(
            modifier = Modifier
                .padding(mediumPadding)
                .height(220.dp),
            gridProperties = GridProperties(
                xAxisProperties = GridProperties.AxisProperties(
                    enabled = true,
                    color = SolidColor(MaterialTheme.colorScheme.outline.copy(0.2f))
                ),
                yAxisProperties = GridProperties.AxisProperties(
                    enabled = true,
                    color = SolidColor(MaterialTheme.colorScheme.outline.copy(0.2f))
                )
            ),
            labelHelperProperties = LabelHelperProperties(
                enabled = false
            ),
            labelProperties = LabelProperties(
                enabled = false,
            ),
            indicatorProperties = HorizontalIndicatorProperties(
                enabled = true,
                textStyle = TextStyle(
                    fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                )
            ),
            data = listOf(
                Line(
                    label = stringResource(id = R.string.current_month),
                    values = dataOfProducts!!,
                    curvedEdges = false,
                    color = SolidColor(MaterialTheme.colorScheme.tertiary),
                    firstGradientFillColor = Color(MaterialTheme.colorScheme.tertiaryContainer.value).copy(
                        alpha = .5f
                    ),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(1250, easing = EaseInOutCubic),
                    gradientAnimationDelay = 800,
                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                    dotProperties = DotProperties(
                        enabled = true,
                        color = SolidColor(MaterialTheme.colorScheme.surface),
                        strokeWidth = 2.dp,
                        radius = 2.dp,
                        strokeColor = SolidColor(MaterialTheme.colorScheme.inverseSurface),
                    )
                )
            ),
            animationMode = AnimationMode.Together(delayBuilder = {
                it * 500L
            }),
        )
    }
}

@Composable
private fun BarsChartYearlySummary(yearlySummaryToBuy: List<MonthlySumStatusZero>?) {

    val barsData = yearlySummaryToBuy?.map { monthlySum ->
        Bars(
            label = monthlySum.month ?: stringResource(id = R.string.empty),
            values = listOf(
                Bars.Data(
                    value = monthlySum.totalSum ?: 0.0,
                    color = SolidColor(MaterialTheme.colorScheme.secondary),
                    properties = BarProperties(spacing = 5.dp)
                )
            )
        )
    }

    Column {
        if (barsData != null) {
            ColumnChart(
                modifier = Modifier
                    .padding(mediumPadding)
                    .height(220.dp),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
                ),
                gridProperties = GridProperties(
                    xAxisProperties = GridProperties.AxisProperties(
                        enabled = true,
                        color = SolidColor(MaterialTheme.colorScheme.outline.copy(0.2f))
                    ),
                    yAxisProperties = GridProperties.AxisProperties(
                        enabled = true,
                        color = SolidColor(MaterialTheme.colorScheme.outline.copy(0.2f))
                    )
                ),
                labelHelperProperties = LabelHelperProperties(
                    enabled = false,
                    textStyle = TextStyle(
                        fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                ),
                labelProperties = LabelProperties(
                    enabled = true,
                    textStyle = TextStyle(
                        fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                ),
                indicatorProperties = HorizontalIndicatorProperties(
                    enabled = true,
                    textStyle = TextStyle(
                        fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                ),
                data = barsData
            )
        }
    }
}

@Composable
private fun CategoryListSummary(monthlyCategorySumToBuy: List<MonthlySumCategoryStatusZero>?) {

    val groupedData = monthlyCategorySumToBuy?.groupBy { it.categoryName }
        ?.mapValues { (categoryName, categoryList) ->
            MonthlySumCategoryStatusZero(
                categoryName = categoryName,
                totalPrice = categoryList.flatMap { it.totalPrice ?: emptyList() }
            )
        }

    Column {
        Text(
            text = stringResource(id = R.string.summary_label),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(smallPadding)
        )

        groupedData?.entries?.forEach { entry ->
            val categoryName = entry.key
            val categoryDataList = entry.value

            CategoryListItem(categoryName, listOf(categoryDataList))
        }
    }
}

@Composable
private fun CategoryListItem(
    categoryName: String?,
    categoryDataList: List<MonthlySumCategoryStatusZero>
) {

    val disableChartPopUp = PopupProperties(
        enabled = false
    )

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surfaceColorAtElevation(
                5.dp,
            ),
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .width(80.dp)
            ) {
                Text(
                    text = categoryName ?: "",
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = "Total value",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                )
            }

            Column {
                if (categoryDataList.isNotEmpty()) {
                    Box(modifier = Modifier.height(40.dp)) {
                        LineChart(
                            modifier = Modifier
                                .width(150.dp)
                                .height(40.dp),
                            data = categoryDataList.map { monthlyCategorySumToBuy ->
                                Line(
                                    label = categoryName ?: stringResource(id = R.string.empty),
                                    values = monthlyCategorySumToBuy.totalPrice ?: emptyList(),
                                    color = SolidColor(MaterialTheme.colorScheme.inverseSurface),
                                    strokeAnimationSpec = tween(1250, easing = EaseInOutCubic),
                                    drawStyle = DrawStyle.Stroke(width = 2.dp),
                                )
                            },
                            popupProperties = disableChartPopUp,
                            dividerProperties = DividerProperties(enabled = false),
                            gridProperties = GridProperties(enabled = false),
                            labelHelperProperties = LabelHelperProperties(enabled = false),
                            indicatorProperties = HorizontalIndicatorProperties(enabled = false),
                            animationMode = AnimationMode.Together(delayBuilder = {
                                it * 500L
                            }),
                        )
                    }
                } else if (categoryDataList.size == 1) {
                    Column {}
                }
            }

            Column(
                modifier = Modifier.padding(start = 10.dp), horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$ " + formatPrice(categoryDataList.sumOf {
                        it.totalPrice?.sum() ?: 0.0
                    }),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Transactions: ${categoryDataList[0].totalPrice?.size ?: 0}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(.4f)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun IncomingScreenPreview() {
    val summaryItemsToBuy = listOf(
        ItemPriceStatusZero(10.0),
        ItemPriceStatusZero(120.0),
        ItemPriceStatusZero(150.0),
        ItemPriceStatusZero(110.0),
        ItemPriceStatusZero(80.0),
        ItemPriceStatusZero(20.0),
    )

    val yearlySummaryToBuy = listOf(
        MonthlySumStatusZero("January", 100.0),
        MonthlySumStatusZero("February", 120.0),
        MonthlySumStatusZero("March", 150.0),
    )

    val monthlyCategorySumToBuy = listOf<MonthlySumCategoryStatusZero>(
        MonthlySumCategoryStatusZero("Art", listOf(100.0)),
    )

    IncomingScreen(summaryItemsToBuy, yearlySummaryToBuy, monthlyCategorySumToBuy)
}