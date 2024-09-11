package com.serranoie.android.buybuddy.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.serranoie.android.buybuddy.ui.backup.BackupScreen
import com.serranoie.android.buybuddy.ui.backup.BackupViewModel
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.edit.EditItemScreen
import com.serranoie.android.buybuddy.ui.edit.EditItemViewModel
import com.serranoie.android.buybuddy.ui.home.HomeScreen
import com.serranoie.android.buybuddy.ui.home.HomeViewModel
import com.serranoie.android.buybuddy.ui.onboard.OnBoardingScreen
import com.serranoie.android.buybuddy.ui.onboard.OnBoardingViewModel
import com.serranoie.android.buybuddy.ui.quiz.QuizFinishedScreen
import com.serranoie.android.buybuddy.ui.quiz.QuizRoute
import com.serranoie.android.buybuddy.ui.settings.AboutScreen
import com.serranoie.android.buybuddy.ui.settings.SettingsScreen
import com.serranoie.android.buybuddy.ui.settings.SettingsViewModel
import com.serranoie.android.buybuddy.ui.summary.SummaryScreen
import com.serranoie.android.buybuddy.ui.summary.SummaryViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    userEventsTracker: UserEventsTracker,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        navigation(route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoarding.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }) {
            composable(route = Route.OnBoarding.route) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(event = viewModel::onEvent)
            }
        }

        navigation(route = Route.HomeNavigation.route,
            startDestination = Route.Home.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            }) {
            composable(route = Route.Home.route) {
                val homeViewModel = hiltViewModel<HomeViewModel>()
                val settingsViewModel = hiltViewModel<SettingsViewModel>()

                val categoriesWithItems by homeViewModel.categoriesWithItems.collectAsState()
                val totalPrice by homeViewModel.totalPrice.collectAsState()
                val totalBoughtPrice by homeViewModel.totalBoughtPrice.collectAsState()
                val categoryVisibility by settingsViewModel.categoryVisibility.collectAsState()

                LaunchedEffect(Unit) {
                    homeViewModel.triggerDataFetch()
                }

                HomeScreen(
                    navController = navController,
                    categoriesWithItems = categoriesWithItems,
                    totalPrice = totalPrice,
                    totalBoughtPrice = totalBoughtPrice,
                    categoryVisibility = categoryVisibility,
                    userEventsTracker = userEventsTracker
                )
            }

            composable(route = Route.Quiz.route, enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            }, exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                )
            }) {
                QuizRoute(
                    userEventsTracker,
                    onNavUp = navController::navigateUp,
                    onQuizComplete = {
                        userEventsTracker.logImportantAction("Quiz completed")
                        navController.navigate(Route.FinishedQuiz.route) {
                            popUpTo(Route.Quiz.route) { inclusive = true }
                        }
                    },
                )
            }

            composable(route = Route.Summary.route, enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }, exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }) {
                val summaryViewModel = hiltViewModel<SummaryViewModel>()

                val summaryItemsToBuy by summaryViewModel.summaryItemsToBuy.collectAsStateWithLifecycle()
                val summaryItemsBought by summaryViewModel.summaryItemsBought.collectAsStateWithLifecycle()
                val yearlySummaryToBuy by summaryViewModel.yearlySummaryToBuy.collectAsStateWithLifecycle()
                val yearlySummaryBought by summaryViewModel.yearlySummaryBought.collectAsStateWithLifecycle()
                val monthlyCategorySumToBuy by summaryViewModel.monthlyCategorySumToBuy.collectAsStateWithLifecycle()
                val monthlyCategorySumBought by summaryViewModel.monthlyCategorySumBought.collectAsStateWithLifecycle()
                val errorState by summaryViewModel.errorState.collectAsStateWithLifecycle()

                val currentMonth = remember {
                    SimpleDateFormat("MM", Locale.getDefault()).format(Date())
                }

                LaunchedEffect(Unit) {
                    summaryViewModel.fetchSummaryItemsToBuy(month = currentMonth)
                    summaryViewModel.fetchSummaryItemsBought(month = currentMonth)
                    summaryViewModel.fetchYearlySummaryToBuy()
                    summaryViewModel.fetchYearlySummaryBought()
                    summaryViewModel.fetchMonthlyCategorySumToBuy(month = currentMonth)
                    summaryViewModel.fetchMonthlyCategorySumBought(month = currentMonth)
                }

                SummaryScreen(
                    navController = navController,
                    summaryItemsToBuy = summaryItemsToBuy,
                    summaryItemsBought = summaryItemsBought,
                    yearlySummaryToBuy = yearlySummaryToBuy,
                    yearlySummaryBought = yearlySummaryBought,
                    monthlyCategorySumToBuy = monthlyCategorySumToBuy,
                    monthlyCategorySumBought = monthlyCategorySumBought,
                    userEventsTracker = userEventsTracker,
                    errorState = errorState
                )
            }

            composable(route = Route.Backup.route, enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }, exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }) {
                val viewModel = hiltViewModel<BackupViewModel>()

                BackupScreen(
                    navController = navController,
                    onGenerateBackupFile = { uri ->
                        viewModel.generateBackupFile(context = navController.context, uri = uri)
                    },
                    onRestoreBackupFile = { uri ->
                        viewModel.restoreBackupFile(context = navController.context, uri = uri)
                    }
                )
            }

            composable(route = Route.FinishedQuiz.route, enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            }, exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            }) {
                QuizFinishedScreen(
                    userEventsTracker = userEventsTracker,
                    onDonePressed = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Home.route) { inclusive = true }
                        }
                    },
                )
            }

            composable(route = "edit/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.IntType }),
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                val coroutineScope = rememberCoroutineScope()

                itemId?.let { id ->
                    val viewModel = hiltViewModel<EditItemViewModel>()

                    LaunchedEffect(id) {
                        viewModel.triggerProductData(id)
                    }

                    EditItemScreen(
                        userEventsTracker = userEventsTracker,
                        navController = navController,
                        isLoading = viewModel.isLoading,
                        productInfo = viewModel.currentItem,
                        categoryInfo = viewModel.categoryInfo,
                        nameItemResponse = viewModel.itemName,
                        onNameItemResponse = viewModel::onItemNameResponse,
                        itemDescription = viewModel.itemDescription,
                        onItemDescriptionResponse = viewModel::onItemDescriptionResponse,
                        itemPrice = viewModel.itemPrice,
                        onItemPriceResponse = viewModel::onItemPriceResponse,
                        itemBenefits = viewModel.itemBenefits,
                        onItemBenefitsResponse = viewModel::onItemBenefitsResponse,
                        itemDisadvantages = viewModel.itemDisadvantages,
                        onItemDisadvantagesResponse = viewModel::onItemDisadvantagesResponse,
                        selectedDateTime = viewModel.selectedDateTime,
                        onSelectedDateTimeResponse = viewModel::onSelectedDateTimeResponse,
                        itemUsage = viewModel.itemUsage,
                        onItemUsageResponse = viewModel::onItemUsageResponse,
                        onItemStatusResponse = { status ->
                            coroutineScope.launch {
                                viewModel.updateItemStatus(itemId, status)
                            }
                        },
                        onUpdateItemEvent = {
                            coroutineScope.launch {
                                viewModel.updateItem(id)
                            }
                        },
                        onDeleteItemEvent = {
                            coroutineScope.launch {
                                viewModel.deleteItem(id)
                            }
                        },
                    )
                }
            }

            navigation(
                route = Route.SettingsNavigation.route, startDestination = Route.Settings.route
            ) {
                composable(route = Route.Settings.route, enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                }, exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }) {
                    SettingsScreen(
                        navController = navController,
                        userEventsTracker = userEventsTracker
                    )
                }

                composable(route = Route.About.route, enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    )
                }, exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    )
                }) {
                    AboutScreen(navController = navController)
                }
            }
        }
    }
}
