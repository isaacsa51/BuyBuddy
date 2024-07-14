package com.serranoie.android.buybuddy.ui.util

import android.os.Build
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL

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
}