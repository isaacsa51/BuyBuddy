package com.serranoie.android.buybuddy.ui.settings

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serranoie.android.buybuddy.ui.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class ThemeMode {
    Light, Dark, Auto
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferenceUtil: PreferenceUtil
) : ViewModel() {

    private val _theme = MutableLiveData(ThemeMode.Auto)
    private val _materialYou = MutableLiveData(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)

    val theme: LiveData<ThemeMode> = _theme
    val materialYou: LiveData<Boolean> = _materialYou

    init {
        _theme.value = ThemeMode.entries.toTypedArray()[getThemeValue()]
        _materialYou.value = getMaterialYouValue()
    }

    fun setTheme(newTheme: ThemeMode) {
        _theme.postValue(newTheme)
        preferenceUtil.putInt(PreferenceUtil.APP_THEME_INT, newTheme.ordinal)
    }

    fun setMaterialYou(newValue: Boolean) {
        _materialYou.postValue(newValue)
        preferenceUtil.putBoolean(PreferenceUtil.MATERIAL_YOU_BOOL, newValue)
    }

    fun setAppLock(newValue: Boolean) {
        preferenceUtil.putBoolean(PreferenceUtil.APP_LOCK_BOOL, newValue)
    }

    fun getThemeValue() = preferenceUtil.getInt(
        PreferenceUtil.APP_THEME_INT, ThemeMode.Auto.ordinal
    )

    fun getMaterialYouValue() = preferenceUtil.getBoolean(
        PreferenceUtil.MATERIAL_YOU_BOOL, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    )

    fun getAppLockValue() = preferenceUtil.getBoolean(
        PreferenceUtil.APP_LOCK_BOOL, false
    )

    /**
     * Get the current theme of the app, regardless of the system theme.
     * This will always return either [ThemeMode.Light] or [ThemeMode.Dark].
     * If user has set the theme to Auto it will return the system theme,
     * again Light or Dark instead of [ThemeMode.Auto].
     */
    @Composable
    fun getCurrentTheme(): ThemeMode {
        return if (theme.value == ThemeMode.Auto) {
            if (isSystemInDarkTheme()) ThemeMode.Dark else ThemeMode.Light
        } else theme.value!!
    }
}