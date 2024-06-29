package com.serranoie.android.buybuddy.ui.navigation

sealed class Route(val route: String) {

    data object AppStartNavigation : Route("appStartNavigation")

    data object HomeNavigation : Route("homeNavigation")

    data object OnBoarding : Route(Screen.ONBOARDING.name)
    data object Home : Route(Screen.HOME.name)
    data object Quiz : Route(Screen.QUIZ.name)
    data object FinishedQuiz : Route(Screen.FINISHED_QUIZ.name)

    data class Edit(val itemId: Int) : Route("edit/$itemId") {
        companion object {
            fun editItemRoute(itemId: Int) = "edit/$itemId"
        }
    }

    data object Settings : Route(Screen.SETTINGS.name)
}