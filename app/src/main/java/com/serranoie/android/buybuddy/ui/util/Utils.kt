package com.serranoie.android.buybuddy.ui.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Utils {

    /**
     * Retrieves the appropriate authenticators based on the Android version.
     *
     * - For Android 9 (Pie) and Android 10 (Q), the authenticators are `BIOMETRIC_WEAK` and `DEVICE_CREDENTIAL`.
     * - For Android 11 (R) and above, the authenticators are `BIOMETRIC_STRONG` and `DEVICE_CREDENTIAL`.
     * - For Android versions below 9, while the authenticators `BIOMETRIC_STRONG` and `DEVICE_CREDENTIAL` are not officially supported,
     *   using them does not result in an error unlike in Android 9 and 10.
     *
     * More details can be found in the
     * [official documentation](https://developer.android.com/reference/androidx/biometric/BiometricPrompt.PromptInfo.Builder#setAllowedAuthenticators(int)).
     *
     * @return The authenticators suitable for the current Android version.
     */
    fun getAuthenticators() = if (Build.VERSION.SDK_INT in 28..29) {
        BIOMETRIC_WEAK or DEVICE_CREDENTIAL
    } else {
        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    }

    /**
     * Open the web link in the browser.
     *
     * @param context The context
     * @param url The URL to open
     */
    fun openWebLink(context: Context, url: String) {
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(intent)
        } catch (exc: ActivityNotFoundException) {
            exc.printStackTrace()
        }
    }

    fun dateToString(date: Date?): String {
        if (date == null) {
            return "Invalid date"
        }
        val outputFormatter = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault())
        outputFormatter.timeZone = TimeZone.getDefault()
        return try {
            outputFormatter.format(date)
        } catch (e: Exception) {
            "Invalid date format"
        }
    }

    fun formatPrice(price: Double): String {
        return String.format("%.2f", price)
    }
}