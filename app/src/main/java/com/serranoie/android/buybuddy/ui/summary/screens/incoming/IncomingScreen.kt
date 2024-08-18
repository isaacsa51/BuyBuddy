package com.serranoie.android.buybuddy.ui.summary.screens.incoming

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.serranoie.android.buybuddy.data.persistance.entity.ItemPriceEntity
import com.serranoie.android.buybuddy.ui.summary.screens.ChartProviderIncoming
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.mediumPadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
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
import ir.ehsannarmani.compose_charts.models.Line
import kotlin.math.absoluteValue

val monthSummaryChart = object : ChartProviderIncoming {
    @Composable
    override fun GetChart(summaryItemsToBuy: List<ItemPriceEntity>) {
        LineChartMonthSummary(summaryItemsToBuy)
    }
}

val yearSummaryChart = object : ChartProviderIncoming {
    @Composable
    override fun GetChart(summaryItemsToBuy: List<ItemPriceEntity>) {
        LineChartYearSummary()
    }
}

@Composable
fun IncomingScreen(summaryItemsToBuy: List<ItemPriceEntity>) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn {
                item { HeaderInformation(summaryItemsToBuy) }

                item { CategoryListSummary() }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HeaderInformation(summaryItemsToBuy: List<ItemPriceEntity>) {
    val charts = listOf(
        monthSummaryChart, yearSummaryChart
    )

    val pagerState = rememberPagerState(pageCount = {
        charts.size
    })


    Column(
        modifier = Modifier
            .padding(vertical = mediumPadding, horizontal = basePadding)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Soon to spend this month", style = MaterialTheme.typography.headlineSmall
        )

        Text(text = "$ 1,201.50", style = MaterialTheme.typography.displayMedium)
    }

    Column(modifier = Modifier.padding(smallPadding)) {

        if (summaryItemsToBuy.size == 1) {
            Card(
                Modifier
                    .height(250.dp)
                    .padding(start = 0.dp, end = basePadding)
            ) {
                LineChartYearSummary()
            }
        } else {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(end = 38.dp),
                pageSize = PageSize.Fill
            ) { page ->
                Card(
                    Modifier
                        .height(250.dp)
                        .padding(start = 0.dp, end = basePadding)
                        .graphicsLayer {
                            val pageOffset =
                                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                            alpha = lerp(
                                start = 0.5f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }) {
                    charts[page].GetChart(summaryItemsToBuy)
                }
            }
        }
    }
}

@Composable
private fun LineChartMonthSummary(summaryItemsToBuy: List<ItemPriceEntity>) {

    val dataOfProducts: List<Double> = summaryItemsToBuy.map { it.price }

    Column {
        LineChart(
            modifier = Modifier
                .padding(horizontal = mediumPadding, vertical = smallPadding)
                .height(220.dp),
            data = listOf(
                Line(
                    label = "Current month",
                    values = dataOfProducts,
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
private fun LineChartYearSummary() {
    val labelHelperProperties = LabelHelperProperties(
        enabled = true, textStyle = MaterialTheme.typography.labelMedium
    )

    Column {
        ColumnChart(
            modifier = Modifier
                .padding(horizontal = mediumPadding, vertical = smallPadding)
                .height(220.dp),
            data = listOf(
                Bars(
                    label = "1", values = listOf(
                        Bars.Data(
                            value = 1.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "2", values = listOf(
                        Bars.Data(
                            value = 50.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "3", values = listOf(
                        Bars.Data(
                            value = 25.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "4", values = listOf(
                        Bars.Data(
                            value = 20.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "5", values = listOf(
                        Bars.Data(
                            value = 100.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "6", values = listOf(
                        Bars.Data(
                            value = 8.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "7", values = listOf(
                        Bars.Data(
                            value = 10.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "8", values = listOf(
                        Bars.Data(
                            value = 10.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "9", values = listOf(
                        Bars.Data(
                            value = 25.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "10", values = listOf(
                        Bars.Data(
                            value = 32.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "11", values = listOf(
                        Bars.Data(
                            value = 15.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
                Bars(
                    label = "12", values = listOf(
                        Bars.Data(
                            value = 152.0, color = SolidColor(MaterialTheme.colorScheme.secondary)
                        )
                    )
                ),
            ),
            labelHelperProperties = labelHelperProperties,
            barProperties = BarProperties(
                spacing = 3.dp,
            ),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
            ),
        )
    }
}

@Composable
private fun CategoryListSummary() {
    Column {
        Text(
            text = "Summary per month",
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
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = "Total value",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(.4f)
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

//@Composable
//@Preview(showBackground = true)
//private fun IncomingScreenPreview() {
//    IncomingScreen(summaryItemsToBuy)
//}