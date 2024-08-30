package com.serranoie.android.buybuddy.ui.core.analytics

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class UserEventsTracker @Inject constructor(private val crashlytics: FirebaseCrashlytics) {
    fun logCurrentScreen(screenName: String) {
        crashlytics.log("User viewed screen: $screenName")
        crashlytics.setCustomKey("screen_name", screenName)
    }

    fun logButtonAction(buttonNameAction: String) {
        crashlytics.log("User clicked button: $buttonNameAction")
        crashlytics.setCustomKey("button_name", buttonNameAction)
    }

    fun logImportantAction(actionName: String) {
        crashlytics.log("User performed action: $actionName")
        crashlytics.setCustomKey("action_name", actionName)
    }

    fun logException(exception: Throwable, screenName: String? = null, additionalInfo: Map<String, String>? = null) {
        crashlytics.recordException(exception)
        screenName?.let { crashlytics.setCustomKey("screen_name", it) }
        additionalInfo?.forEach { (key, value) -> crashlytics.setCustomKey(key, value) }
    }

    fun logDataLoadingError(dataSource: String, errorMessage: String? = null) {
        crashlytics.log("Data loading error from $dataSource")
        crashlytics.setCustomKey("data_source", dataSource)
        errorMessage?.let { crashlytics.setCustomKey("error_message", it) }
    }

    fun logDeviceAndEnvironmentInfo(context: Context) {
        crashlytics.setCustomKey("device_model", Build.MODEL)
        crashlytics.setCustomKey("os_version", Build.VERSION.RELEASE)
        crashlytics.setCustomKey("screen_resolution", "${context.resources.displayMetrics.widthPixels}x${context.resources.displayMetrics.heightPixels}")

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val networkType = when {
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "Wi-Fi"
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "Cellular"
            else -> "Unknown"
        }
        crashlytics.setCustomKey("network_type", networkType)
    }

    fun logAdditionalInfo(value: String) {
        crashlytics.log(value)
    }
}