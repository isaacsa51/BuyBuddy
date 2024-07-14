package com.serranoie.android.buybuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.serranoie.android.buybuddy.ui.edit.EditItemScreen
import com.serranoie.android.buybuddy.ui.home.HomeScreen
import com.serranoie.android.buybuddy.ui.onboard.OnBoardingScreen
import com.serranoie.android.buybuddy.ui.onboard.OnBoardingViewModel
import com.serranoie.android.buybuddy.ui.quiz.QuizFinishedScreen
import com.serranoie.android.buybuddy.ui.quiz.QuizRoute
import com.serranoie.android.buybuddy.ui.settings.AboutScreen
import com.serranoie.android.buybuddy.ui.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoarding.route,
        ) {
            composable(route = Route.OnBoarding.route) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(event = viewModel::onEvent)
            }
        }

        navigation(route = Route.HomeNavigation.route, startDestination = Route.Home.route) {
            composable(route = Route.Home.route) {
                HomeScreen(navController = navController)
            }

            composable(route = Route.Quiz.route) {
                QuizRoute(
                    onNavUp = navController::navigateUp,
                    onQuizComplete = {
                        navController.navigate(Route.FinishedQuiz.route) {
                            popUpTo(Route.Quiz.route) { inclusive = true }
                        }
                    },
                )
            }

            composable(route = Route.FinishedQuiz.route) {
                QuizFinishedScreen(
                    onDonePressed = {
                        navController.navigate(Route.Home.route) {
                            popUpTo(Route.Home.route) { inclusive = true }
                        }
                    },
                )
            }

            composable(
                route = "edit/{itemId}",
                arguments = listOf(navArgument("itemId") { type = NavType.IntType }),
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getInt("itemId")
                itemId?.let {
                    EditItemScreen(navController = navController, itemId = it)
                }
            }

            composable(route = Route.Settings.route) {
                SettingsScreen(navController = navController)
            }

            composable(route = Route.About.route) {
                AboutScreen(navController = navController)
            }
        }
    }
}
