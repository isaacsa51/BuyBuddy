package com.serranoie.android.buybuddy.ui.core.biometrics

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.fragment.app.FragmentActivity
import com.serranoie.android.buybuddy.R

class BiometricAuthenticator(
    private val context: Context
) {
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val biometricManager = BiometricManager.from(context)
    private lateinit var biometricPrompt: BiometricPrompt

    private fun isBiometricAuthAvailable(): BiometricAuthStatus {
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthStatus.READY
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAuthStatus.NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED
            else -> BiometricAuthStatus.NOT_AVAILABLE
        }
    }

    @SuppressLint("RestrictedApi")
    fun promptBiometricAuth(
        title: String,
        subtitle: String,
        negativeButtonText: String,
        fragmentActivity: FragmentActivity,
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onFailed: () -> Unit,
        onError: (errorCode: Int, errorString: String) -> Unit,
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            biometricPrompt = BiometricPrompt(
                fragmentActivity,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onSuccess(result)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        onError(errorCode, errString.toString())
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        onFailed()
                    }
                }
            )

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setNegativeButtonText(negativeButtonText)
                .build()
            biometricPrompt.authenticate(promptInfo)
        } else {
            val fingerprintManager = FingerprintManagerCompat.from(context)

            if (fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()) {
                val fragmentManager = fragmentActivity.supportFragmentManager
                val fingerprintDialog = FingerprintAuthenticationDialogFragment()
                fingerprintDialog.show(fragmentManager, "fingerprint_dialog")
            } else {
                onError(
                    BiometricAuthStatus.NOT_AVAILABLE.id,
                    context.getString(R.string.biometric_not_available)
                )
            }
        }

        when (isBiometricAuthAvailable()) {
            BiometricAuthStatus.NOT_AVAILABLE -> {
                onError(
                    BiometricAuthStatus.NOT_AVAILABLE.id,
                    context.getString(R.string.biometric_not_available)
                )
                return
            }

            BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE -> {
                onError(
                    BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE.id,
                    context.getString(R.string.biometric_temporary_not_available)
                )
                return
            }

            BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED -> {
                onError(
                    BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED.id,
                    context.getString(R.string.biometric_available_not_enrolled)
                )
                return
            }

            else -> Unit
        }
    }
}