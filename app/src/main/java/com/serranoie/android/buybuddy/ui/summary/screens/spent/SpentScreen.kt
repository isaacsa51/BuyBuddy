package com.serranoie.android.buybuddy.ui.summary.screens.spent

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusOne
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusOne
import com.serranoie.android.buybuddy.ui.common.EmptySummary
import com.serranoie.android.buybuddy.ui.summary.screens.ChartProvider
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
import kotlin.math.absoluteValue

val monthSummaryChart = object : ChartProvider {
    @Composable
    override fun GetChart(
        summaryItemsBought: List<ItemPriceStatusOne>?,
        yearlySummaryBought: List<MonthlySumStatusOne>?
    ) {
        LineChartMonthSummary(summaryItemsBought)
    }
}

val yearSummaryChart = object : ChartProvider {
    @Composable
    override fun GetChart(
        summaryItemsBought: List<ItemPriceStatusOne>?,
        yearlySummaryBought: List<MonthlySumStatusOne>?
    ) {
        BarsChartYearlySummary(yearlySummaryBought)
    }
}

@Composable
fun SpentScreen(
    summaryItemsBought: List<ItemPriceStatusOne>?,
    yearlySummaryBought: List<MonthlySumStatusOne>?
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                item { HeaderInformation(summaryItemsBought, yearlySummaryBought) }

                item { CategoryListSummary() }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HeaderInformation(
    summaryItemsBought: List<ItemPriceStatusOne>?,
    yearlySummaryBought: List<MonthlySumStatusOne>?
) {

    if (summaryItemsBought?.isEmpty() == true || yearlySummaryBought?.isEmpty() == true) {
        EmptySummary()
    } else {
        val charts = listOf(
            monthSummaryChart, yearSummaryChart
        )

        val pagerState = rememberPagerState(pageCount = {
            charts.size
        })

        var totalSpent = 0.0
        summaryItemsBought?.forEach { item ->
            totalSpent += item.price ?: 0.0
        }

        Column {
            Column(
                modifier = Modifier
                    .padding(vertical = mediumPadding, horizontal = basePadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Total spent this month",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "$ " + formatPrice(totalSpent),
                    style = MaterialTheme.typography.displayMedium
                )
            }

            Column(modifier = Modifier.padding(smallPadding)) {

                if (summaryItemsBought?.size!! <= 1) {
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
                        // TODO: Why is this crashing but showing correctly even commented?
                        BarsChartYearlySummary(yearlySummaryBought)
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
                            charts[page].GetChart(summaryItemsBought, yearlySummaryBought)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LineChartMonthSummary(summaryItemsBought: List<ItemPriceStatusOne>?) {

    val dataOfProducts: List<Double>? = summaryItemsBought?.map { it.price ?: 0.0 }

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
                    label = "Current month",
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
private fun BarsChartYearlySummary(yearlySummaryBought: List<MonthlySumStatusOne>?) {

    val barsData = yearlySummaryBought?.map { monthlySum ->
        Bars(
            label = monthlySum.month ?: "Empty",
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
private fun CategoryListSummary() {
    Column {
        Text(
            text = "Summary per category",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(smallPadding)
        )

        CategoryListItem()
    }
}

@Composable
private fun CategoryListItem() {

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
                    text = "Category",
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = "Total value",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(.5f)
                )
            }

            Column {
                LineChart(
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp),
                    data = listOf(
                        Line(
                            label = "Art",
                            values = listOf(28.0, 41.0, 5.0, 10.0, 35.0),
                            color = SolidColor(MaterialTheme.colorScheme.inverseSurface),
                            strokeAnimationSpec = tween(1250, easing = EaseInOutCubic),
                            drawStyle = DrawStyle.Stroke(width = 2.dp),
                        )
                    ),
                    dividerProperties = DividerProperties(enabled = false),
                    gridProperties = GridProperties(enabled = false),
                    labelHelperProperties = LabelHelperProperties(enabled = false),
                    indicatorProperties = HorizontalIndicatorProperties(enabled = false),
                    animationMode = AnimationMode.Together(delayBuilder = {
                        it * 500L
                    }),
                )
            }

            Column(
                modifier = Modifier.padding(start = 10.dp), horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$12,321.50",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Transactions: 12",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(.4f)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SpentScreenPreview() {
    val summaryItemsBought = listOf(
        ItemPriceStatusOne(10.0),
        ItemPriceStatusOne(120.0),
        ItemPriceStatusOne(150.0),
        ItemPriceStatusOne(110.0),
        ItemPriceStatusOne(80.0),
        ItemPriceStatusOne(20.0),
    )

    val yearlySummaryBought = listOf(
        MonthlySumStatusOne("January", 100.0),
        MonthlySumStatusOne("February", 120.0),
        MonthlySumStatusOne("March", 150.0),
    )

    Scaffold {
        Column(Modifier.padding(it)) {
            HeaderInformation(summaryItemsBought, yearlySummaryBought)

            CategoryListItem()
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun EmptySummaryPreview() {
    val summaryItemsBought = listOf<ItemPriceStatusOne>()

    val yearlySummaryBought = listOf<MonthlySumStatusOne>()

    Scaffold {
        Column(Modifier.padding(it)) {
            HeaderInformation(summaryItemsBought, yearlySummaryBought)

            CategoryListItem()
        }
    }
}