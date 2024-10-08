package com.serranoie.android.buybuddy.domain.usecase.appentry

import com.serranoie.android.buybuddy.domain.manager.LocalUserManager
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckThemeUseCase(private val localUserManager: LocalUserManager) {
    operator fun invoke(): Flow<UseCaseResult<Boolean>> = flow {
        try {
            localUserManager.checkTheme()
        } catch (e: Exception) {
            emit(UseCaseResult.Error(e))
        }
    }
}