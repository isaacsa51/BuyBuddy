package com.serranoie.android.buybuddy.ui.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BrightnessMedium
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.serranoie.android.buybuddy.BuildConfig
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.core.MainActivity
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.navigation.Screen
import com.serranoie.android.buybuddy.ui.settings.common.SettingsCategory
import com.serranoie.android.buybuddy.ui.settings.common.SettingsContainer
import com.serranoie.android.buybuddy.ui.settings.common.SettingsItem
import com.serranoie.android.buybuddy.ui.settings.common.SettingsItemSwitch
import com.serranoie.android.buybuddy.ui.settings.common.ThemePickerDialog
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import com.serranoie.android.buybuddy.ui.util.getActivity
import com.serranoie.android.buybuddy.ui.util.toToast
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, userEventsTracker: UserEventsTracker) {
    val view = LocalView.current
    val context = LocalContext.current
    val viewModel = (context.getActivity() as MainActivity).settingsViewModel
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(Unit) {
        userEventsTracker.logCurrentScreen("settings_screen")
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_title),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        view.weakHapticFeedback()
                        userEventsTracker.logButtonAction("back_button")
                        navController.navigateUp()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
        ) {
            item { DisplaySettings(viewModel = viewModel, userEventsTracker) }

            item { BehaviourSettings(viewModel = viewModel, userEventsTracker) }

            item { InfoSettings(navController = navController, userEventsTracker) }

            if (BuildConfig.DEBUG) {
                item {
                    Button(onClick = {
                        simulateError()
                    }) {
                        Text(text = "Throw Error")
                    }
                }
            }

        }
    }
}

private fun simulateError() {
    throw RuntimeException("Simulated error for demonstration purposes.")
}

@Composable
fun DisplaySettings(viewModel: SettingsViewModel, userEventsTracker: UserEventsTracker) {
    val context = LocalContext.current
    val showThemeSheet = remember { mutableStateOf(false) }

    val themeValue = when (viewModel.getThemeValue()) {
        ThemeMode.Light.ordinal -> stringResource(id = R.string.theme_dialog_option1)
        ThemeMode.Dark.ordinal -> stringResource(id = R.string.theme_dialog_option2)
        else -> stringResource(id = R.string.theme_dialog_option3)
    }

    val materialYouValue = remember {
        mutableStateOf(viewModel.getMaterialYouValue())
    }

    Spacer(modifier = Modifier.height(smallPadding))

    SettingsContainer {
        SettingsCategory(title = stringResource(id = R.string.display_settings_title))

        SettingsItem(
            title = stringResource(id = R.string.theme_setting),
            description = stringResource(id = R.string.theme_setting_desc),
            icon = Icons.Rounded.BrightnessMedium,
            onClick = {
                showThemeSheet.value = true
                userEventsTracker.logButtonAction("theme_button")
            },
        )

        SettingsItemSwitch(
            title = stringResource(id = R.string.material_you_setting),
            description = stringResource(id = R.string.material_you_setting_desc),
            icon = Icons.Rounded.Palette,
            switchState = materialYouValue,
            onCheckChange = { newValue ->
                materialYouValue.value = newValue

                userEventsTracker.logButtonAction("material_you_toggle: $newValue")

                if (newValue) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        viewModel.setMaterialYou(true)
                    } else {
                        materialYouValue.value = false
                        context.getString(R.string.material_you_error).toToast(context)
                    }
                } else {
                    viewModel.setMaterialYou(false)
                }
            },
        )

        if (showThemeSheet.value) {
            ThemePickerDialog(
                themeValue = themeValue,
                showThemeDialog = showThemeSheet,
                onThemeChange = { newTheme ->
                    viewModel.setTheme(newTheme)
                },
            )
        }
    }
}

@Composable
fun InfoSettings(navController: NavController, userEventsTracker: UserEventsTracker) {
    SettingsContainer {
        SettingsCategory(title = stringResource(id = R.string.misc_setting_title))

        SettingsItem(
            title = stringResource(id = R.string.app_info_setting),
            description = stringResource(id = R.string.app_info_setting_desc),
            icon = Icons.Rounded.Info,
            onClick = {
                userEventsTracker.logButtonAction("about_button")
                navController.navigate(Screen.ABOUT.name)
            },
        )
    }
    Spacer(modifier = Modifier.height(2.dp))
}

@Composable
fun BehaviourSettings(viewModel: SettingsViewModel, userEventsTracker: UserEventsTracker) {

    val categoryVisibilityValue =
        remember { mutableStateOf(viewModel.getCategoryVisibilityValue()) }

    val appLockValue = remember { mutableStateOf(viewModel.getAppLockValue()) }

    SettingsContainer {
        SettingsCategory(title = stringResource(id = R.string.behaviour_label))

        SettingsItemSwitch(title = stringResource(R.string.show_empty_categories_label),
            description = stringResource(R.string.empty_categories_desc),
            icon = Icons.Rounded.RemoveCircleOutline,
            switchState = categoryVisibilityValue,
            onCheckChange = { newValue ->
                userEventsTracker.logButtonAction("show_empty_categories_toggle: $newValue")
                categoryVisibilityValue.value = newValue
                viewModel.setCategoryVisibility(newValue)
            }
        )

        SettingsItemSwitch(title = stringResource(R.string.app_lock_screen_title),
            description = stringResource(R.string.app_lock_setting_desc),
            icon = Icons.Rounded.Lock,
            switchState = appLockValue,
            onCheckChange = { newValue ->
                userEventsTracker.logButtonAction("app_lock_toggle: $newValue")
                appLockValue.value = newValue
                viewModel.setAppLock(newValue)
            }
        )
    }
}