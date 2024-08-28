package com.serranoie.android.buybuddy.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import com.serranoie.android.buybuddy.ui.common.CategoryCard
import com.serranoie.android.buybuddy.ui.common.EmptyListScreen
import com.serranoie.android.buybuddy.ui.common.TotalAmountCard
import com.serranoie.android.buybuddy.ui.core.MainActivity
import com.serranoie.android.buybuddy.ui.navigation.NavigationItem
import com.serranoie.android.buybuddy.ui.navigation.Route
import com.serranoie.android.buybuddy.ui.navigation.Screen
import com.serranoie.android.buybuddy.ui.settings.ThemeMode
import com.serranoie.android.buybuddy.ui.util.getActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    val items = listOf(
        NavigationItem(
            title = stringResource(id = R.string.drawer_home),
            selectedIcon = Icons.Rounded.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = Screen.HOME.name
        ),
        NavigationItem(
            title = stringResource(id = R.string.summary_home),
            selectedIcon = Icons.Rounded.BarChart,
            unselectedIcon = Icons.Outlined.BarChart,
            route = Screen.SUMMARY.name
        ),

        // TODO: Implement backup functionality to the app
        /*NavigationItem(
            title = stringResource(id = R.string.backup_home),
            selectedIcon = Icons.Rounded.Backup,
            unselectedIcon = Icons.Outlined.Backup,
            route = Screen.BACKUP.name
        ),*/
        NavigationItem(
            title = stringResource(id = R.string.settings_home),
            selectedIcon = Icons.Rounded.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = Screen.SETTINGS.name
        ),
        NavigationItem(
            title = stringResource(id = R.string.rate_us_home),
            selectedIcon = Icons.Rounded.ThumbUp,
            unselectedIcon = Icons.Outlined.ThumbUp,
            route = Screen.HOME.name
        ),
    )

    val context = LocalContext.current
    val settingsViewModel = (context.getActivity() as MainActivity).settingsViewModel

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    Surface {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(295.dp),
                    drawerShape = RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp),
                    drawerTonalElevation = 2.dp,
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    DrawerHeader(themeMode = settingsViewModel.getCurrentTheme())

                    Divider()

                    Spacer(modifier = Modifier.height(16.dp))

                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = { Text(item.title) },
                            selected = selectedItemIndex == index,
                            onClick = {
                                navController.navigate(item.route)
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
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
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(Icons.Rounded.Menu, contentDescription = "Open Menu")
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
                            contentDescription = stringResource(R.string.create_item_label)
                        )
                    }
                }
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

                            categoriesWithItems.filter { categoryWithItems ->
                                categoryWithItems.items.isNotEmpty() || categoryVisibility
                            }.forEachIndexed { index, categoryWithItems ->
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
    }
}

@Composable
private fun DrawerHeader(themeMode: ThemeMode) {
    Row(
        modifier = Modifier
            .height(140.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(20.dp))

        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = if (themeMode == ThemeMode.Light) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                    shape = CircleShape
                )
        ) {
            AsyncImage(
                model = R.drawable.ic_launcher,
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.fillMaxSize(),
            )
        }
        Spacer(modifier = Modifier.width(18.dp))

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
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
            ), items = listOf(
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
                ), ItemEntity(
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
        ), CategoryWithItemsEntity(
            category = CategoryEntity(
                categoryId = 2,
                name = "Category 2",
            ), items = listOf(
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
                ), ItemEntity(
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
