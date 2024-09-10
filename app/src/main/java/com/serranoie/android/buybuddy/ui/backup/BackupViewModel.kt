package com.serranoie.android.buybuddy.ui.backup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.serranoie.android.buybuddy.data.backup.BackupData
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.ui.util.UiConstants.BACKUP_FILE_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val dao: BuyBuddyDao
) : ViewModel() {

    fun generateBackupFile(context: Context) {
        viewModelScope.launch {
            val backupData = getAllDataForBackup()

            val jsonData = convertToJson(backupData)
            Timber.i("JSON Data: $jsonData")

            saveJsonToFile(context, jsonData)
        }
    }

    fun restoreBackupFile(context: Context) {
        // TODO: Implement restore logic
    }

    // TODO: Map from entity to domain
    private suspend fun getAllDataForBackup(): BackupData {
        val categoriesWithItems = dao.getCategoriesWithItems().first()
        val categories = categoriesWithItems.map { it.category }
        val items = dao.getAllItems().first()
        return BackupData(categoriesWithItems = categoriesWithItems, categories = categories, items = items)
    }

    private fun convertToJson(backupData: BackupData): String {
        val gson = Gson()
        return gson.toJson(backupData)
    }

    private fun saveJsonToFile(context: Context, jsonData: String) {
        val fileName = BACKUP_FILE_NAME
        val file = File(context.getExternalFilesDir(null), fileName)
        file.writeText(jsonData)
    }

}