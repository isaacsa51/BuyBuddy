package com.serranoie.android.buybuddy.ui.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.ui.common.CategoryCard
import com.serranoie.android.buybuddy.ui.common.EmptyListScreen
import com.serranoie.android.buybuddy.ui.common.TotalAmountCard
import com.serranoie.android.buybuddy.ui.navigation.Route
import com.serranoie.android.buybuddy.ui.navigation.Screen
import kotlinx.coroutines.delay
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    categoriesWithItems: List<CategoryWithItemsEntity>,
    totalPrice: Double,
    totalBoughtPrice: Double,
    categoryVisibility: Boolean,
) {
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
            Box(modifier = Modifier.padding(padding)) {
                Column(modifier = Modifier.fillMaxSize()) {
                    TotalAmountCard(
                        totalPrice = totalPrice,
                        totalBoughtPrice = totalBoughtPrice,
                        modifier = Modifier.testTag("TotalAmountCard"),
                    )

                    categoriesWithItems
                        .filter { categoryWithItems ->
                            categoryWithItems.items.isNotEmpty() || categoryVisibility
                        }
                        .forEachIndexed { index, categoryWithItems ->
                            val visibleState = remember { mutableStateOf(false) }

                            LaunchedEffect(key1 = index) {
                                delay(index * 100L)
                                visibleState.value = true
                            }

                            AnimatedVisibility(
                                visible = visibleState.value,
                                enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                                exit = fadeOut() + slideOutVertically(),
                            ) {
                                CategoryCard(
                                    categoryWithItems = categoryWithItems,
                                    navController = navController,
                                )
                            }
                        }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val n = rememberNavController()
    val categoriesWithItems = listOf(
        CategoryWithItemsEntity(
            category = CategoryEntity(
                categoryId = 1,
                name = "Category 1",
            ),
            items = listOf(
                ItemEntity(
                    itemId = 1,
                    name = "Item 1",
                    categoryId = 1,
                    description = "Description",
                    usage = "Usage",
                    benefits = "Benefits",
                    disadvantages = "Disadvantages",
                    price = 100.0,
                    reminderDate = Date(),
                    reminderTime = Date(),
                    status = false
                ),
                ItemEntity(
                    itemId = 2,
                    name = "Item 2",
                    categoryId = 1,
                    description = "Description",
                    usage = "Usage",
                    benefits = "Benefits",
                    disadvantages = "Disadvantages",
                    price = 100.0,
                    reminderDate = Date(),
                    reminderTime = Date(),
                    status = false
                )
            )
        ),
        CategoryWithItemsEntity(
            category = CategoryEntity(
                categoryId = 2,
                name = "Category 2",
            ),
            items = listOf(
                ItemEntity(
                    itemId = 1,
                    name = "Item 3",
                    categoryId = 3,
                    description = "Description",
                    usage = "Usage",
                    benefits = "Benefits",
                    disadvantages = "Disadvantages",
                    price = 100.0,
                    reminderDate = Date(),
                    reminderTime = Date(),
                    status = false
                ),
                ItemEntity(
                    itemId = 2,
                    name = "Item 4",
                    categoryId = 4,
                    description = "Description",
                    usage = "Usage",
                    benefits = "Benefits",
                    disadvantages = "Disadvantages",
                    price = 100.0,
                    reminderDate = Date(),
                    reminderTime = Date(),
                    status = false
                )
            )
        )
    )

    Surface {
        HomeScreen(
            navController = n,
            categoriesWithItems = categoriesWithItems,
            totalPrice = 500.0,
            totalBoughtPrice = 300.0,
            categoryVisibility = true
        )
    }
}
