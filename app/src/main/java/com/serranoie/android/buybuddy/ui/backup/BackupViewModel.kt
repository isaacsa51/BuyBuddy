package com.serranoie.android.buybuddy.ui.backup

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.serranoie.android.buybuddy.data.backup.BackupData
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val dao: BuyBuddyDao
) : ViewModel() {

    fun generateBackupFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            val backupData = getAllDataForBackup()

            val jsonData = convertToJson(backupData)
            Timber.i("JSON Data: $jsonData")

            saveJsonToFile(context, uri, jsonData)
        }
    }

    fun restoreBackupFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            val jsonData = readJsonFromFile(context, uri)

            if (jsonData != null) {
                val backupData = parseJsonToBackupData(jsonData)
                insertBackupDataToDatabase(backupData)
            } else {
                Timber.e("Failed to read JSON data from file.")
            }
        }
    }

    private fun readJsonFromFile(context: Context, uri: Uri): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
        } catch (e: Exception) {
            Timber.e(e, "Error reading backup file")
            null
        }
    }

    private fun parseJsonToBackupData(jsonData: String): BackupData {
        val gson = Gson()
        return gson.fromJson(jsonData, BackupData::class.java)
    }

    private suspend fun insertBackupDataToDatabase(backupData: BackupData) {
        backupData.categoriesWithItems.forEach { categoryWithItems ->
            dao.insertCategory(categoryWithItems.category)
            categoryWithItems.items.forEach { item ->
                dao.insertItem(item)
            }
        }
    }

    // TODO: Map from entity to domain
    private suspend fun getAllDataForBackup(): BackupData {
        val categoriesWithItems = dao.getCategoriesWithItems().first()
        val categories = categoriesWithItems.map { it.category }
        val items = dao.getAllItems().first()

        return BackupData(
            categoriesWithItems = categoriesWithItems,
            categories = categories,
            items = items
        )
    }

    private fun convertToJson(backupData: BackupData): String {
        val gson = Gson()
        return gson.toJson(backupData)
    }

    private fun saveJsonToFile(context: Context, uri: Uri, jsonData: String) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonData.toByteArray())
                Timber.d("Backup file saved to selected location.")
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to save backup file")
        }
    }
}