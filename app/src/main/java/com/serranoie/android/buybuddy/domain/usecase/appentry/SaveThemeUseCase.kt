package com.serranoie.android.buybuddy.domain.usecase.appentry

import com.serranoie.android.buybuddy.domain.manager.LocalUserManager

class SaveThemeUseCase(private val localUserManager: LocalUserManager) {
    suspend operator fun invoke(enabled: Boolean) {
        localUserManager.saveTheme(enabled)
    }
}