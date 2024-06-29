package com.serranoie.android.buybuddy.domain.usecase.appentry

import com.serranoie.android.buybuddy.domain.manager.LocalUserManager

class SaveAppEntry(private val localUserManager: LocalUserManager) {
    suspend operator fun invoke() {
        localUserManager.saveAppEntry()
    }
}