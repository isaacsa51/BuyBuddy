package com.serranoie.android.buybuddy.ui.settings

import android.os.Build
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class ThemeMode {
    Light, Dark, Auto
}



@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {

//    fun getThemeValue() = preferenceUtil.getInt(
//        PreferenceUtil.APP_THEME_INT, ThemeMode.Auto.ordinal
//    )
//
//    fun getMaterialYouValue() = preferenceUtil.getBoolean(
//        PreferenceUtil.MATERIAL_YOU_BOOL, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
//    )
//
//    fun getAppLockValue() = preferenceUtil.getBoolean(
//        PreferenceUtil.APP_LOCK_BOOL, false
//    )
}