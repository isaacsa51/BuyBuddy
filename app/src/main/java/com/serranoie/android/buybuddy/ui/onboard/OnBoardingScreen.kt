package com.serranoie.android.buybuddy.ui.onboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.common.PageIndicator
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.mediumPadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.pageIndicatorWidth
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    event: (OnBoardingEvent) -> Unit,
) {
    val context = LocalContext.current
    
    val pagerState = rememberPagerState(initialPage = 0) {
        pages.size
    }
    val buttonState = remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> listOf("", context.getString(R.string.next))
                1 -> listOf(context.getString(R.string.back), context.getString(R.string.get_started))
                else -> listOf("", "")
            }
        }
    }

    Scaffold(
        content = @Composable { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                HorizontalPager(state = pagerState) { index ->
                    OnBoardingPage(page = pages[index])
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        },
        bottomBar = {
            Column(modifier = Modifier.padding(vertical = basePadding)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = mediumPadding)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PageIndicator(
                        modifier = Modifier.width(pageIndicatorWidth),
                        pageSize = pages.size,
                        selectedPage = pagerState.currentPage,
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val scope = rememberCoroutineScope()

                        if (buttonState.value[0].isNotEmpty()) {
                            TextButton(
                                modifier = Modifier
                                    .weight(0.4f)
                                    .height(48.dp)
                                    .padding(horizontal = smallPadding),
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(page = pagerState.currentPage - 1)
                                    }
                                },
                            ) {
                                Text(text = buttonState.value[0])
                            }
                        }

                        Button(
                            modifier = Modifier
                                .weight(0.6f)
                                .height(48.dp)
                                .padding(horizontal = basePadding),
                            onClick = {
                                scope.launch {
                                    if (pagerState.currentPage == 1) {
                                        event(OnBoardingEvent.SaveAppEntry)
                                    } else {
                                        pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                                    }
                                }
                            },
                        ) {
                            Text(text = buttonState.value[1])
                        }
                    }
                }
            }
        }
    )
}