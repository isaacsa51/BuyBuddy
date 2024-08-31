package com.serranoie.android.buybuddy

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.serranoie.android.buybuddy.ui.core.analytics.CrashlyticsTree
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.util.NotificationConstants.RMNDR_NOTI_CHNNL_ID
import com.serranoie.android.buybuddy.ui.util.NotificationConstants.RMNDR_NOTI_CHNNL_NAME
import com.serranoie.android.buybuddy.ui.util.NotificationConstants.RMNDR_NOTI_ID
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BuyBuddyApplication : Application() {

    @Inject
    lateinit var userEventsTracker: UserEventsTracker

    override fun onCreate() {
        super.onCreate()

        initTimber()

        userEventsTracker.logDeviceAndEnvironmentInfo(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    private fun initTimber() = when {
        BuildConfig.DEBUG -> {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return super.createStackElementTag(element) + ":" + element.lineNumber
                }
            })
        }
        else -> {
            Timber.plant(CrashlyticsTree(this))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = RMNDR_NOTI_CHNNL_NAME
        val descriptionText = RMNDR_NOTI_ID
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel(RMNDR_NOTI_CHNNL_ID, name, importance).apply {
                description = descriptionText
                setShowBadge(true)
            }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
