package com.serranoie.android.buybuddy.ui.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private val preferenceUtil: PreferenceUtil
) : ViewModel() {

    private val _theme = MutableLiveData(ThemeMode.Auto)
    private val _materialYou = MutableLiveData(false)
    private val _categoryVisibility = MutableStateFlow(false)

    val theme: LiveData<ThemeMode> = _theme
    val materialYou: LiveData<Boolean> = _materialYou
    val categoryVisibility: StateFlow<Boolean> = _categoryVisibility

    init {
        _theme.value = ThemeMode.entries.toTypedArray()[getThemeValue()]
        _materialYou.value = getMaterialYouValue()
        _categoryVisibility.value = getCategoryVisibilityValue()
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
