package com.serranoie.android.buybuddy.ui.core

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.core.theme.BuyBuddyTheme
import com.serranoie.android.buybuddy.ui.home.HomeViewModel
import com.serranoie.android.buybuddy.ui.navigation.NavGraph
import com.serranoie.android.buybuddy.ui.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    private val onBoardViewModel by viewModels<AppEntryViewModel>()

    val settingsViewModel by viewModels<SettingsViewModel>()

    @Inject lateinit var userEventsTracker: UserEventsTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        }

        setContent {
            BuyBuddyTheme(settingsViewModel = settingsViewModel) {
                navController = rememberNavController()
                Surface(
                    modifier =
                        Modifier
                            .imePadding()
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
            }
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
