package com.serranoie.android.buybuddy.ui.settings

import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.BrightnessMedium
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.core.theme.BuyBuddyTheme
import com.serranoie.android.buybuddy.ui.settings.common.SettingsItem
import com.serranoie.android.buybuddy.ui.settings.common.SettingsItemSwitch
import com.serranoie.android.buybuddy.ui.util.UiConstants.smallPadding
import com.serranoie.android.buybuddy.ui.util.toToast
import com.serranoie.android.buybuddy.ui.util.weakHapticFeedback
import java.util.concurrent.Executor

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val view = LocalView.current
    val context = LocalContext.current
//    val viewModel = (context.getActivity() as MainActivity).settingsViewModel

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        view.weakHapticFeedback()
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
            item { DisplaySettings() }

            item { SecuritySettings() }

            item { InfoSettings() }

            item {
                SettingsContainer {
                    Button(
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                        onClick = { throw RuntimeException("Firebase Crash log!") },
                    ) {
                        Text("CRASH")
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsContainer(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.padding(smallPadding),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceColorAtElevation(
                        3.dp,
                    ),
            ),
    ) {
        Column(modifier = Modifier.padding(top = 2.dp)) {
            content()
        }
    }
}

@Composable
private fun SettingsCategory(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        modifier =
        Modifier
            .padding(vertical = 8.dp)
            .padding(horizontal = 14.dp),
    )
}

@Composable
fun DisplaySettings() {
    val context = LocalContext.current
    val showThemeSheet = remember { mutableStateOf(false) }

//    val themeValue = when (viewModel.getThemeValue()) {
//        ThemeMode.Light.ordinal -> stringResource(id = R.string.theme_dialog_option1)
//        ThemeMode.Dark.ordinal -> stringResource(id = R.string.theme_dialog_option2)
//        else -> stringResource(id = R.string.theme_dialog_option3)
//    }

    val materialYouValue = remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(smallPadding))

    SettingsContainer {
        SettingsCategory(title = stringResource(id = R.string.display_settings_title))

        SettingsItem(
            title = stringResource(id = R.string.theme_setting),
            description = stringResource(id = R.string.theme_setting_desc),
            icon = Icons.Rounded.BrightnessMedium,
            onClick = { showThemeSheet.value = true },
        )

        SettingsItemSwitch(
            title = stringResource(id = R.string.material_you_setting),
            description = stringResource(id = R.string.material_you_setting_desc),
            icon = Icons.Rounded.Palette,
            switchState = materialYouValue,
            onCheckChange = { newValue ->
                materialYouValue.value = newValue
                if (newValue) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        true
                    } else {
                        materialYouValue.value = false
                        context.getString(R.string.material_you_error).toToast(context)
                    }
                } else {
                    false
                }
            },
        )

//        if (showThemeSheet.value) {
//            ThemePickerDialog(
//                themeValue = themeValue,
//                showThemeDialog = showThemeSheet,
//                onThemeChange = { newTheme ->
//                    newTheme
//                }
//            )
//        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun SecuritySettings() {
    val context = LocalContext.current
    val appLockSwitch = remember { mutableStateOf(false) }

    lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
//    lateinit var promptInfo: BiometricPrompt.PromptInfo

    SettingsContainer {
        SettingsCategory(title = stringResource(id = R.string.security_settings_title))
        SettingsItemSwitch(
            title = stringResource(id = R.string.security_app_lock),
            description = stringResource(id = R.string.app_lock_setting_desc),
            icon = Icons.Filled.Lock,
            switchState = appLockSwitch,
            onCheckChange = { newValue ->
                newValue
//                appLockSwitch.value = newValue
//                if (newValue) {
//                    val mainActivity = context.getActivity() as MainActivity
//                    executor = ContextCompat.getMainExecutor(context)
//                    biometricPrompt = BiometricPrompt(mainActivity,
//                        executor,
//                        object : BiometricPrompt.AuthenticationCallback() {
//                            override fun onAuthenticationError(
//                                errorCode: Int, errString: CharSequence
//                            ) {
//                                super.onAuthenticationError(errorCode, errString)
//                                context.getString(R.string.auth_error).format(errString)
//                                    .toToast(context)
//                                // disable preference switch manually on auth error.
//                                appLockSwitch.value = false
//                            }
//
//                            override fun onAuthenticationSucceeded(
//                                result: BiometricPrompt.AuthenticationResult
//                            ) {
//                                super.onAuthenticationSucceeded(result)
//                                context.getString(R.string.auth_successful).toToast(context)
//                                mainActivity.mainViewModel.setAppUnlocked(true)
//                                viewModel.setAppLock(true)
//                            }
//
//                            override fun onAuthenticationFailed() {
//                                super.onAuthenticationFailed()
//                                context.getString(R.string.auth_failed).toToast(context)
//                                // disable preference switch manually on auth error.
//                                appLockSwitch.value = false
//                            }
//                        })
//
//                    promptInfo = BiometricPrompt.PromptInfo.Builder()
//                        .setTitle(context.getString(R.string.bio_lock_title))
//                        .setSubtitle(context.getString(R.string.bio_lock_subtitle))
//                        .setAllowedAuthenticators(Utils.getAuthenticators()).build()
//
//                    biometricPrompt.authenticate(promptInfo)
//                } else {
//                    viewModel.setAppLock(false)
//                }
            },
        )
    }
}

@Composable
fun InfoSettings() {
    SettingsContainer {
        SettingsCategory(title = stringResource(id = R.string.misc_setting_title))

        SettingsItem(
            title = stringResource(id = R.string.license_setting),
            description = stringResource(id = R.string.license_setting_desc),
            icon = Icons.Filled.LocalPolice,
            onClick = { },
        )
        SettingsItem(
            title = stringResource(id = R.string.app_info_setting),
            description = stringResource(id = R.string.app_info_setting_desc),
            icon = Icons.Filled.Info,
            onClick = { },
        )
    }
    Spacer(modifier = Modifier.height(2.dp))
}

@RequiresApi(Build.VERSION_CODES.P)
@PreviewLightDark
@Composable
fun SettingsScreenPreview() {
    BuyBuddyTheme {
        SettingsScreen(navController = NavController(LocalContext.current))
    }
}
