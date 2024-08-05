package com.serranoie.android.buybuddy.ui.summary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.common.CustomTabIndicator
import com.serranoie.android.buybuddy.ui.common.noRippleClickable
import com.serranoie.android.buybuddy.ui.summary.screens.spent.SpentScreen
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.extraSmallPadding
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SummaryScreen(navController: NavController) {
    val view = LocalView.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    val tabRowItems = listOf(
        SummaryItem(label = "Spent", screen = { SpentScreen() }),
        SummaryItem(label = "Incoming", screen = { IncomingScreen() }),
    )

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Summary",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        view.weakHapticFeedback()
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
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            TabRow(
                modifier = Modifier
                    .padding(top = basePadding, bottom = extraSmallPadding, start = basePadding, end = basePadding)
                    .clip(RoundedCornerShape(50)),
                selectedTabIndex = pagerState.currentPage,
                divider = { },
                indicator = { tabPositions ->
                    CustomTabIndicator(tabPositions = tabPositions, pagerState = pagerState)
                }
            ) {
                tabRowItems.forEachIndexed { index, item ->
                    Tab(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .noRippleClickable { },
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } }
                    ) {
                        Text(text = item.label)
                    }
                }
            }

            HorizontalPager(state = pagerState, userScrollEnabled = false) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    tabRowItems[pagerState.currentPage].screen()
                }
            }
        }
    }
}

@Composable
private fun IncomingScreen() {
    Column {
        Text("Incoming")
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryScreenPreview() {
    val navController = rememberNavController()
    SummaryScreen(navController)
}