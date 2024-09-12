package com.serranoie.android.buybuddy.ui.core

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.review.ReviewManagerFactory
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.core.biometrics.BiometricAuthenticator
import com.serranoie.android.buybuddy.ui.core.biometrics.BlockedAppScreen
import com.serranoie.android.buybuddy.ui.core.biometrics.FingerprintAuthCallback
import com.serranoie.android.buybuddy.ui.core.biometrics.FingerprintAuthenticationDialogFragment
import com.serranoie.android.buybuddy.ui.core.notification.ReminderReceiver
import com.serranoie.android.buybuddy.ui.core.theme.BuyBuddyTheme
import com.serranoie.android.buybuddy.ui.navigation.NavGraph
import com.serranoie.android.buybuddy.ui.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalLayoutApi::class)
@AndroidEntryPoint
class MainActivity : FragmentActivity(), FingerprintAuthCallback {
    private lateinit var navController: NavHostController

    private val onBoardViewModel by viewModels<AppEntryViewModel>()

    val settingsViewModel by viewModels<SettingsViewModel>()

    private lateinit var showContentState: MutableState<Boolean>

    @Inject
    lateinit var userEventsTracker: UserEventsTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showContentState = mutableStateOf(!settingsViewModel.getAppLockValue())

        enableEdgeToEdge(
            statusBarStyle =
            SystemBarStyle.auto(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb(),
            ),
            navigationBarStyle =
            SystemBarStyle.auto(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb(),
            ),
        )

        actionBar?.hide()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                onBoardViewModel.splashCondition
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        } else {
            requestExternalStoragePermission(this)
        }

        setContent {
            BuyBuddyTheme(settingsViewModel = settingsViewModel) {
                val layoutDirection = LocalLayoutDirection.current
                val displayCutout = WindowInsets.displayCutout.asPaddingValues()
                val startPadding = displayCutout.calculateStartPadding(layoutDirection)
                val endPadding = displayCutout.calculateEndPadding(layoutDirection)

                val biometricAuthenticator = remember { BiometricAuthenticator(this) }
                navController = rememberNavController()

                val authenticate: () -> Unit = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        biometricAuthenticator.promptBiometricAuth(
                            title = getString(R.string.bio_lock_title),
                            subtitle = getString(R.string.app_lock_setting_desc),
                            negativeButtonText = getString(R.string.cancel),
                            fragmentActivity = this,
                            onSuccess = { showContentState.value = true },
                            onFailed = { showContentState.value = false },
                            onError = { _, errorString ->
                                showContentState.value = false
                                Toast.makeText(
                                    this,
                                    getString(R.string.auth_error, errorString),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    } else {
                        showFingerprintDialog()
                    }
                }

                LaunchedEffect(Unit) {
                    if (settingsViewModel.getAppLockValue()) {
                        authenticate()
                    }
                }

                if (showContentState.value) {
                    Surface(
                        modifier = Modifier
                            .padding(
                                PaddingValues(
                                    start = startPadding,
                                    end = endPadding,
                                    bottom = displayCutout.calculateBottomPadding()
                                )
                            )
                            .imePadding()
                            .imeNestedScroll()
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,

                    ) {
                        val startDestination = onBoardViewModel.startDestination
                        NavGraph(navController, startDestination, userEventsTracker)

                        intent?.getStringExtra("nav_route")?.let { navRoute ->
                            Timber.i("User navigated via Notification to product: $navRoute")
                            navController.navigate(navRoute)
                        }
                    }
                } else {
                    BlockedAppScreen(onUnlockPressed = authenticate)
                }
            }
        }
    }

    private fun showFingerprintDialog() {
        val fingerprintDialog = FingerprintAuthenticationDialogFragment()
        fingerprintDialog.show(supportFragmentManager, "FingerprintDialog")
    }

    override fun onAuthenticationSucceeded(success: Boolean) {
        Timber.i("Authentication succeeded: $success")
        showContentState.value = success
    }

    private fun requestExternalStoragePermission(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                0
            )
        }
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                ReminderReceiver.REQUEST_POST_NOTIFICATIONS_PERMISSION,
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ReminderReceiver.REQUEST_POST_NOTIFICATIONS_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Timber.i("Permission granted, proceed with showing the notification")
                } else {
                    // Permission denied, handle the denial
                    Toast
                        .makeText(
                            this,
                            "Permission denied to post notifications",
                            Toast.LENGTH_SHORT,
                        ).show()
                    Timber.e("Permission Denied to post notifications!")
                }
                return
            }
        }
    }
}

fun Activity.launchInAppReview(
    onComplete: (() -> Unit)? = null,
) {
    val reviewManager = ReviewManagerFactory.create(this)
    val request = reviewManager.requestReviewFlow()
    request.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val reviewInfo = task.result
            val flow = reviewManager.launchReviewFlow(this, reviewInfo)
            flow.addOnCompleteListener {
                // The flow has finished. The API doesn't indicate whether the user
                // reviewed or not, or even whether the review dialog was shown.
                // Therefore, no matter the result, continue with your app's flow.
                onComplete?.invoke()
            }
        } else {
            onComplete?.invoke()
        }
    }
}