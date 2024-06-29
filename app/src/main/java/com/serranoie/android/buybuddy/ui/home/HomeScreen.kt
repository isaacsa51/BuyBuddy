package com.serranoie.android.buybuddy.ui.home

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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(), navController: NavController
) {
    val categoriesWithItems by viewModel.categoriesWithItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val totalBoughtPrice by viewModel.totalBoughtPrice.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())


    BuyBuddyTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.SETTINGS.name) }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Add")
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Route.Quiz.route) },
                containerColor = MaterialTheme.colorScheme.tertiary,
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.create_item_label)
                )
            }
        }, modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { padding ->
            if (categoriesWithItems.isEmpty() || categoriesWithItems.all { it.items.isEmpty() }) {
                EmptyListScreen(padding)
            } else {
                Column(
                    modifier = Modifier.padding(padding)
                ) {
                    TotalAmountCard(totalPrice = totalPrice, totalBoughtPrice = totalBoughtPrice)

                    LazyColumn {
                        items(categoriesWithItems.filter { it.items.isNotEmpty() }) {
                            CategoryCard(categoryWithItems = it, navController = navController)
                        }
                    }
                }
            }
        }
    }
}