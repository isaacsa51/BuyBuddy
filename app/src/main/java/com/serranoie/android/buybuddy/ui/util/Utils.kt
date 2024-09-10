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