package com.serranoie.android.buybuddy.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.common.CategoryCard
import com.serranoie.android.buybuddy.ui.common.EmptyListScreen
import com.serranoie.android.buybuddy.ui.common.TotalAmountCard
import com.serranoie.android.buybuddy.ui.core.theme.BuyBuddyTheme
import com.serranoie.android.buybuddy.ui.navigation.Route
import com.serranoie.android.buybuddy.ui.navigation.Screen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
) {
    val categoriesWithItems by viewModel.categoriesWithItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val totalBoughtPrice by viewModel.totalBoughtPrice.collectAsState()

    BuyBuddyTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                        )
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(Screen.SETTINGS.name) }) {
                            Icon(Icons.Outlined.Settings, contentDescription = "Add")
                        }
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Route.Quiz.route) },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = stringResource(R.string.create_item_label),
                    )
                }
            },
        ) { padding ->
            if (categoriesWithItems.isEmpty() || categoriesWithItems.all { it.items.isEmpty() }) {
                EmptyListScreen(padding)
            } else {
                Column(modifier = Modifier.padding(padding)) {
                    TotalAmountCard(
                        totalPrice = totalPrice,
                        totalBoughtPrice = totalBoughtPrice,
                        modifier = Modifier.testTag("TotalAmountCard")
                    )

                    categoriesWithItems.forEachIndexed { index, categoryWithItems ->
                        val visibleState = remember { mutableStateOf(false) }
                        LaunchedEffect(key1 = index) {
                            delay(index * 80L)
                            visibleState.value = true
                        }

                        AnimatedVisibility(
                            visible = visibleState.value,
                            enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                            exit = fadeOut() + slideOutVertically()
                        ) {
                            CategoryCard(categoryWithItems = categoryWithItems, navController = navController)
                        }
                    }
                }
            }
        }
    }
}
