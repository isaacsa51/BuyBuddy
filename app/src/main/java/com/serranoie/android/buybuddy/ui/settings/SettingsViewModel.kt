package com.serranoie.android.buybuddy.ui.settings

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.annotation.VisibleForTesting
import androidx.biometric.BiometricManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.serranoie.android.buybuddy.ui.core.analytics.UserEventsTracker
import com.serranoie.android.buybuddy.ui.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

enum class ThemeMode {
    Light, Dark, Auto
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceUtil: PreferenceUtil,
    private val userEventsTracker: UserEventsTracker
) : ViewModel() {

    private val _theme = MutableStateFlow(ThemeMode.Auto)
    val theme: StateFlow<ThemeMode> = _theme

    private val _materialYou = MutableStateFlow(false)
    val materialYou: StateFlow<Boolean> = _materialYou

    private val _categoryVisibility = MutableStateFlow(false)
    val categoryVisibility: StateFlow<Boolean> = _categoryVisibility

    init {
        _theme.value = ThemeMode.entries.toTypedArray()[getThemeValue()]
        _materialYou.value = getMaterialYouValue()
        _categoryVisibility.value = getCategoryVisibilityValue()

        logSettingsInfo()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun logSettingsInfo() {
        val additionalInfo = mapOf(
            "theme" to theme.value.toString(),
            "materialYou" to materialYou.value.toString(),
            "categoryVisibility" to categoryVisibility.value.toString()
        )
        userEventsTracker.logLoadedSettingsInfo("settings_loaded", additionalInfo)
    }

    fun setTheme(newTheme: ThemeMode) {
        _theme.value = newTheme
        preferenceUtil.putInt(PreferenceUtil.APP_THEME_INT, newTheme.ordinal)
    }

    fun setMaterialYou(newValue: Boolean) {
        _materialYou.value = newValue
        preferenceUtil.putBoolean(PreferenceUtil.MATERIAL_YOU_BOOL, newValue)
    }

    fun setAppLock(newValue: Boolean) {
        preferenceUtil.putBoolean(PreferenceUtil.APP_LOCK_BOOL, newValue)
    }

    fun setCategoryVisibility(newValue: Boolean) {
        _categoryVisibility.value
        preferenceUtil.putBoolean(PreferenceUtil.CATEGORY_VISIBILITY_BOOL, newValue)
    }

    fun getThemeValue() = preferenceUtil.getInt(
        PreferenceUtil.APP_THEME_INT, ThemeMode.Auto.ordinal
    )

    fun getMaterialYouValue() = preferenceUtil.getBoolean(
        PreferenceUtil.MATERIAL_YOU_BOOL, false
    )

    fun getAppLockValue() = preferenceUtil.getBoolean(
        PreferenceUtil.APP_LOCK_BOOL, false
    )

    fun getCategoryVisibilityValue() =
        preferenceUtil.getBoolean(PreferenceUtil.CATEGORY_VISIBILITY_BOOL, false)

    @Composable
    fun getCurrentTheme(): ThemeMode {
        val currentTheme = theme.collectAsState()

        return if (currentTheme.value == ThemeMode.Auto) {
            if (isSystemInDarkTheme()) ThemeMode.Dark else ThemeMode.Light
        } else currentTheme.value
    }

    fun isBiometricAvailable(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val biometricManager = BiometricManager.from(context)
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
        } else {
            val fingerprintManager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()
        }
    }
}
