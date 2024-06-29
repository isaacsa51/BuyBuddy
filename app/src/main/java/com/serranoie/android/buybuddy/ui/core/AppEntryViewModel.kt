package com.serranoie.android.buybuddy.ui.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serranoie.android.buybuddy.domain.usecase.appentry.AppEntryUseCase
import com.serranoie.android.buybuddy.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppEntryViewModel @Inject constructor(
    private val appEntryUseCase: AppEntryUseCase,
) : ViewModel() {
    var splashCondition by mutableStateOf(true)
    var startDestination by mutableStateOf(Route.AppStartNavigation.route)

    var dynamicColorsEnabled by mutableStateOf(false)

    init {
        appEntryUseCase.readAppEntry().onEach { shouldStartFromHome ->
            startDestination = if (shouldStartFromHome) {
                Route.HomeNavigation.route
            } else {
                Route.AppStartNavigation.route
            }
            splashCondition = false
        }.launchIn(viewModelScope)

        appEntryUseCase.checkTheme().onEach { isEnabled ->
            dynamicColorsEnabled = isEnabled
        }.launchIn(viewModelScope)
    }

    fun toggleDynamicColors(enabled: Boolean) {
        viewModelScope.launch {
            appEntryUseCase.saveTheme(enabled)
        }
    }
}