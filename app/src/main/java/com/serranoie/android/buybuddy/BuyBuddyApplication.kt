package com.serranoie.android.buybuddy

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.serranoie.android.buybuddy.ui.util.Constants.RMNDR_NOTI_CHNNL_ID
import com.serranoie.android.buybuddy.ui.util.Constants.RMNDR_NOTI_CHNNL_NAME
import com.serranoie.android.buybuddy.ui.util.Constants.RMNDR_NOTI_ID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BuyBuddyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
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
