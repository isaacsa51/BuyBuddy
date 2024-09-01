package com.serranoie.android.buybuddy.ui.core

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.ui.util.NotificationConstants.RMNDR_NOTI_ITEM_ID_KEY
import com.serranoie.android.buybuddy.ui.util.NotificationConstants.RMNDR_NOTI_MESSAGE_KEY
import com.serranoie.android.buybuddy.ui.util.NotificationConstants.RMNDR_NOTI_TITLE_KEY
import java.util.Calendar
import java.util.Date

class ScheduleNotification {
    fun scheduleNotification(
        context: Context,
        itemId: Int,
        itemName: String,
        reminderDate: Date?,
        reminderTime: Date?,
    ) {
        val intent = Intent(context.applicationContext, ReminderReceiver::class.java).apply {
            putExtra(RMNDR_NOTI_TITLE_KEY, context.getString(R.string.notification_title))
            putExtra(
                RMNDR_NOTI_MESSAGE_KEY,
                context.getString(R.string.notification_body, itemName)
            )
            putExtra(RMNDR_NOTI_ITEM_ID_KEY, itemId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext, itemId, intent, PendingIntent.FLAG_MUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()

        reminderDate?.let {
            calendar.time = it
            reminderTime?.let { time ->
                calendar.set(Calendar.HOUR_OF_DAY, time.hours)
                calendar.set(Calendar.MINUTE, time.minutes)
            }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
            )
        }
    }
}
