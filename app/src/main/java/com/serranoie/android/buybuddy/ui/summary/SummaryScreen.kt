package com.serranoie.android.buybuddy.ui.summary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusOne
import com.serranoie.android.buybuddy.domain.model.ItemPriceStatusZero
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusOne
import com.serranoie.android.buybuddy.domain.model.MonthlySumCategoryStatusZero
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusOne
import com.serranoie.android.buybuddy.domain.model.MonthlySumStatusZero
import com.serranoie.android.buybuddy.ui.common.CustomTabIndicator
import com.serranoie.android.buybuddy.ui.common.noRippleClickable
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.summary.screens.incoming.IncomingScreen
import com.serranoie.android.buybuddy.ui.summary.screens.spent.SpentScreen
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SummaryScreen(
    navController: NavController,
    summaryItemsToBuy: List<ItemPriceStatusZero>?,
    summaryItemsBought: List<ItemPriceStatusOne>?,
    yearlySummaryToBuy: List<MonthlySumStatusZero>?,
    yearlySummaryBought: List<MonthlySumStatusOne>?,
    monthlyCategorySumToBuy: List<MonthlySumCategoryStatusZero>?,
    monthlyCategorySumBought: List<MonthlySumCategoryStatusOne>?,
    errorState: String?,
    userEventsTracker: UserEventsTracker,
) {
    val view = LocalView.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    val tabRowItems = listOf(
        SummaryItem(label = stringResource(id = R.string.spent_title), screen = {
            SpentScreen(
                summaryItemsBought, yearlySummaryBought, monthlyCategorySumBought, userEventsTracker
            )
        }),
        SummaryItem(label = stringResource(id = R.string.incoming_title), screen = {
            IncomingScreen(
                summaryItemsToBuy, yearlySummaryToBuy, monthlyCategorySumToBuy, userEventsTracker
            )
        }),
    )

    LaunchedEffect(Unit) {
        userEventsTracker.logCurrentScreen("summary_screen")
    }

    Scaffold(
        topBar = {
            Column {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.summary_screen_title),
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            view.weakHapticFeedback()
                            userEventsTracker.logButtonAction("back_button")
                            navController.navigateUp()
                        }) {
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = stringResource(id = R.string.back),
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
                TabRow(
                    modifier = Modifier
                        .padding(
                            top = smallPadding, start = basePadding, end = basePadding
                        )
                        .background(Color.Transparent)
                        .clip(RoundedCornerShape(50)),
                    selectedTabIndex = pagerState.currentPage,
                    divider = { },
                    indicator = { tabPositions ->
                        CustomTabIndicator(tabPositions = tabPositions, pagerState = pagerState)
                    }) {
                    tabRowItems.forEachIndexed { index, item ->
                        Tab(modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .noRippleClickable { },
                            selected = pagerState.currentPage == index,
                            onClick = {

                                userEventsTracker.logButtonAction("tab_button")
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        ) {
                            Text(text = item.label)
                        }
                    }
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            HorizontalPager(state = pagerState, userScrollEnabled = false) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    if (errorState != null) {
                        Timber.e(errorState)
                        Text(
                            text = errorState,
                            style = MaterialTheme.typography.displaySmall.copy(textAlign = TextAlign.Center)
                        )
                    } else {
                        tabRowItems[pagerState.currentPage].screen()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryScreenPreview() {
    val navController = rememberNavController()
    val crashlytics = FirebaseCrashlytics.getInstance()
    val userEventsTracker = UserEventsTracker(crashlytics)

    val summaryItemsToBuy = listOf(
        ItemPriceStatusZero(50.0),
        ItemPriceStatusZero(8.0),
        ItemPriceStatusZero(10.0),
        ItemPriceStatusZero(12.0),
        ItemPriceStatusZero(23.0)
    )

    val summaryItemsBought = listOf(
        ItemPriceStatusOne(50.0),
        ItemPriceStatusOne(8.0),
        ItemPriceStatusOne(10.0),
        ItemPriceStatusOne(12.0),
        ItemPriceStatusOne(23.0)
    )

    val yearlySummaryToBuy = listOf(
        MonthlySumStatusZero("January", 50.0),
        MonthlySumStatusZero("February", 8.0),
        MonthlySumStatusZero("March", 10.0),
        MonthlySumStatusZero("April", 12.0),
        MonthlySumStatusZero("May", 23.0)
    )
    val yearlySummaryBought = listOf(
        MonthlySumStatusOne("January", 50.0),
        MonthlySumStatusOne("February", 8.0),
        MonthlySumStatusOne("March", 10.0),
        MonthlySumStatusOne("April", 12.0),
        MonthlySumStatusOne("May", 23.0)
    )

    SummaryScreen(
        navController,
        summaryItemsToBuy,
        summaryItemsBought,
        yearlySummaryToBuy,
        yearlySummaryBought,
        null,
        null,
        null,
        userEventsTracker
    )
}