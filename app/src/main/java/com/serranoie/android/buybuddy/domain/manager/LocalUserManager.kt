package com.serranoie.android.buybuddy.domain.manager

import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveAppEntry()
    fun readAppEntry(): Flow<UseCaseResult<Boolean>>
    fun checkTheme(): Flow<UseCaseResult<Boolean>>
    suspend fun saveTheme(enabled: Boolean)
}