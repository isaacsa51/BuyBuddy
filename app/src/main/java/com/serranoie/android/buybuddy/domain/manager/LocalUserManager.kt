package com.serranoie.android.buybuddy.domain.manager

import kotlinx.coroutines.flow.Flow

interface LocalUserManager {
    suspend fun saveAppEntry()
    fun readAppEntry(): Flow<Boolean>
    fun checkTheme(): Flow<Boolean>
    suspend fun saveTheme(enabled: Boolean)
}