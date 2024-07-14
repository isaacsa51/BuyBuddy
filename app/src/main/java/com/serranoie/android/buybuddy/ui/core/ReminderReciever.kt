package com.serranoie.android.buybuddy.ui.core

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.NotificationConstants.RMNDR_NOTI_CHNNL_ID
import com.serranoie.android.buybuddy.ui.util.NotificationConstants.RMNDR_NOTI_ITEM_ID_KEY
import com.serranoie.android.buybuddy.ui.util.NotificationConstants.RMNDR_NOTI_MESSAGE_KEY
import com.serranoie.android.buybuddy.ui.util.NotificationConstants.RMNDR_NOTI_TITLE_KEY

class ReminderReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val title = intent.getStringExtra(RMNDR_NOTI_TITLE_KEY)
        val message = intent.getStringExtra(RMNDR_NOTI_MESSAGE_KEY)
        val itemId = intent.getIntExtra(RMNDR_NOTI_ITEM_ID_KEY, -1)

        val notificationBuilder =
            NotificationCompat
                .Builder(context, RMNDR_NOTI_CHNNL_ID)
                .setSmallIcon(R.drawable.ic_buybuddy_mono)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_POST_NOTIFICATIONS_PERMISSION,
                )
            }
            return
        }
        notificationManager.notify(itemId, notificationBuilder.build())
    }

    companion object {
        const val REQUEST_POST_NOTIFICATIONS_PERMISSION = 1001
    }
}
