package com.serranoie.android.buybuddy.data.persistance.prefs.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.serranoie.android.buybuddy.data.util.Constants
import com.serranoie.android.buybuddy.data.util.Constants.USER_SETTINGS
import com.serranoie.android.buybuddy.domain.manager.LocalUserManager
import com.serranoie.android.buybuddy.domain.usecase.UseCaseResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserManagerImpl(private val context: Context) : LocalUserManager {
    override suspend fun saveAppEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = true
        }
    }

    override fun readAppEntry(): Flow<UseCaseResult<Boolean>> {
        return context.dataStore.data.map { preferences ->
            try{
                UseCaseResult.Success(preferences[PreferenceKeys.APP_ENTRY] ?: false)
            } catch (e: Exception) {
                UseCaseResult.Error(e)
            }
        }
    }

    override fun checkTheme(): Flow<UseCaseResult<Boolean>> {
        return context.dataStore.data.map { preferences ->
            try {
                UseCaseResult.Success(preferences[PreferenceKeys.STATUS_THEME] ?: false)
            } catch (e: Exception) {
                UseCaseResult.Error(e)
            }
        }
    }

    override suspend fun saveTheme(enabled: Boolean) {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.STATUS_THEME] = enabled
        }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_SETTINGS)

private object PreferenceKeys {
    val APP_ENTRY = booleanPreferencesKey(name = Constants.APP_ENTRY)
    val STATUS_THEME = booleanPreferencesKey(name = Constants.CHECK_THEME)
}
