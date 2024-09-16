package com.serranoie.android.buybuddy.ui.home

import android.app.Activity
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
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Backup
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.android.play.core.review.ReviewManagerFactory
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.ui.common.CategoryCard
import com.serranoie.android.buybuddy.ui.common.EmptyListScreen
import com.serranoie.android.buybuddy.ui.common.TotalAmountCard
import com.serranoie.android.buybuddy.ui.core.MainActivity
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.core.launchInAppReview
import com.serranoie.android.buybuddy.ui.navigation.NavigationItem
import com.serranoie.android.buybuddy.ui.navigation.Route
import com.serranoie.android.buybuddy.ui.navigation.Screen
import com.serranoie.android.buybuddy.ui.settings.ThemeMode
import com.serranoie.android.buybuddy.ui.util.UiConstants.basePadding
import com.serranoie.android.buybuddy.ui.util.UiConstants.drawerAppSpacing
import com.serranoie.android.buybuddy.ui.util.UiConstants.drawerHeaderHeight
import com.serranoie.android.buybuddy.ui.util.UiConstants.drawerIconSize
import com.serranoie.android.buybuddy.ui.util.UiConstants.modalDrawerShape
import com.serranoie.android.buybuddy.ui.util.UiConstants.modalDrawerWidth
import com.serranoie.android.buybuddy.ui.util.getActivity
import com.serranoie.android.buybuddy.ui.util.strongHapticFeedback
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    categoriesWithItems: List<CategoryWithItemsEntity>,
    totalPrice: Double,
    totalBoughtPrice: Double,
    categoryVisibility: Boolean,
    userEventsTracker: UserEventsTracker
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

        NavigationItem(
            title = stringResource(id = R.string.backup_home),
            selectedIcon = Icons.Rounded.Backup,
            unselectedIcon = Icons.Outlined.Backup,
            route = Screen.BACKUP.name
        ),
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

    val view = LocalView.current
    val context = LocalContext.current
    val settingsViewModel = (context.getActivity() as MainActivity).settingsViewModel
    val manager = ReviewManagerFactory.create(context)
    val request = manager.requestReviewFlow()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        userEventsTracker.logCurrentScreen("HomeScreen")
    }

    Surface {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(modalDrawerWidth),
                    drawerShape = RoundedCornerShape(topEnd = modalDrawerShape, bottomEnd = modalDrawerShape),
                    drawerTonalElevation = 2.dp,
                ) {
                    Spacer(modifier = Modifier.height(basePadding))

                    DrawerHeader(themeMode = settingsViewModel.getCurrentTheme())

                    Divider()

                    Spacer(modifier = Modifier.height(basePadding))

                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = { Text(item.title) },
                            selected = selectedItemIndex == index,
                            onClick = {
                                view.weakHapticFeedback()

                                userEventsTracker.logButtonAction("drawer_button: ${item.title}")

                                // Creating condition to determine to trigger review flow or navigation
                                if (item.title == getString(context, R.string.rate_us_home)) {
                                    val activity = context.getActivity() as? Activity

                                    activity?.launchInAppReview()
                                } else {
                                    navController.navigate(item.route)
                                }
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
                                        view.weakHapticFeedback()
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
                        onClick = {
                            view.strongHapticFeedback()
                            userEventsTracker.logImportantAction("new_product")
                            navController.navigate(Route.Quiz.route)
                        },
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
            .height(drawerHeaderHeight)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(drawerAppSpacing))

        Box(
            modifier = Modifier
                .size(drawerIconSize)
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

        Spacer(modifier = Modifier.width(basePadding))

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
