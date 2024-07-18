package com.serranoie.android.buybuddy.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.ui.edit.EditItemScreen
import com.serranoie.android.buybuddy.ui.home.HomeScreen
import com.serranoie.android.buybuddy.ui.home.HomeViewModel
import com.serranoie.android.buybuddy.ui.onboard.OnBoardingScreen
import com.serranoie.android.buybuddy.ui.onboard.OnBoardingViewModel
import com.serranoie.android.buybuddy.ui.quiz.QuizFinishedScreen
import com.serranoie.android.buybuddy.ui.quiz.QuizRoute
import com.serranoie.android.buybuddy.ui.settings.AboutScreen
import com.serranoie.android.buybuddy.ui.settings.SettingsScreen
import com.serranoie.android.buybuddy.ui.settings.SettingsViewModel

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
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

                val categoriesWithItems =
                    remember { mutableStateOf<List<CategoryWithItemsEntity>>(emptyList()) }
                val totalPrice = remember { mutableDoubleStateOf(0.0) }
                val totalBoughtPrice = remember { mutableDoubleStateOf(0.0) }
                val categoryVisibility by settingsViewModel.categoryVisibility.collectAsState()

                LaunchedEffect(Unit) {
                    homeViewModel.triggerDataFetch()

                    snapshotFlow { homeViewModel.categoriesWithItems }
                        .collect { categoriesWithItems.value = it }
                    snapshotFlow { homeViewModel.totalPrice }
                        .collect { totalPrice.doubleValue = it }
                    snapshotFlow { homeViewModel.totalBoughtPrice }
                        .collect { totalBoughtPrice.doubleValue = it }
                }

                HomeScreen(
                    navController = navController,
                    categoriesWithItems = categoriesWithItems.value,
                    totalPrice = totalPrice.doubleValue,
                    totalBoughtPrice = totalBoughtPrice.doubleValue,
                    categoryVisibility = categoryVisibility,
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
                    onNavUp = navController::navigateUp,
                    onQuizComplete = {
                        navController.navigate(Route.FinishedQuiz.route) {
                            popUpTo(Route.Quiz.route) { inclusive = true }
                        }
                    },
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
                itemId?.let {
                    EditItemScreen(navController = navController, itemId = it)
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
                    SettingsScreen(navController = navController)
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
