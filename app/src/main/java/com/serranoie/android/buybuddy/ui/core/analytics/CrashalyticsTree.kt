package com.serranoie.android.buybuddy.ui.core.analytics

import android.content.Context
import android.os.Build
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.serranoie.android.buybuddy.BuildConfig
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CrashlyticsTree(context: Context) : Timber.Tree() {

    private val crashlytics = FirebaseCrashlytics.getInstance()

    init {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val userId = dateFormat.format(currentDate) + Build.MODEL
        crashlytics.setUserId(userId)
        sendCustomKeys(context)
    }

    private fun sendCustomKeys(context: Context) {
        val builder = CustomKeysAndValues.Builder()
        builder.putString("abi", Build.SUPPORTED_ABIS[0])
        builder.putString("locale", getLocale(context).toString())
        builder.putBoolean("isGooglePlayServicesAvailable", isGooglePlayServicesAvailable(context))
        getInstallSource(context)?.let {
            builder.putString("installSource", it)
        }
        crashlytics.setCustomKeys(builder.build())
    }

    private fun getLocale(context: Context): Locale {
        return context.resources.configuration.locales[0]
    }

    private fun isGooglePlayServicesAvailable(context: Context): Boolean {
        return GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
    }

    private fun getInstallSource(context: Context): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return context.packageManager.getInstallSourceInfo(context.packageName).initiatingPackageName
        }
        return null
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }

        if (BuildConfig.DEBUG) {
            crashlytics.setCrashlyticsCollectionEnabled(true)
            return
        }

        crashlytics.setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)

        if (tag != null) {
            crashlytics.setCustomKey(CRASHLYTICS_KEY_TAG, tag)
        }
        crashlytics.setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)

        if (t == null) {
            crashlytics.recordException(Exception(message))
        } else {
            crashlytics.recordException(t)
        }
    }

    companion object {
        private const val CRASHLYTICS_KEY_PRIORITY = "priority"
        private const val CRASHLYTICS_KEY_TAG = "tag"
        private const val CRASHLYTICS_KEY_MESSAGE = "message"
    }
}