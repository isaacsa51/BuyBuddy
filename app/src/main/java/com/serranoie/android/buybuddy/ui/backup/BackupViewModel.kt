package com.serranoie.android.buybuddy.ui.backup

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSyntaxException
import com.serranoie.android.buybuddy.R
import com.serranoie.android.buybuddy.data.backup.BackupData
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val dao: BuyBuddyDao,
    application: Application
) : AndroidViewModel(application) {

    private val _backupResultState = MutableStateFlow<BackupResultState>(BackupResultState.Idle)
    val backupResultState: StateFlow<BackupResultState> = _backupResultState

    fun resetBackupResultState() {
        _backupResultState.value = BackupResultState.Idle
    }

    fun generateBackupFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            val backupData = getAllDataForBackup()
            val jsonData = convertToJson(backupData)

            saveJsonToFile(context, uri, jsonData)
        }
    }

    fun restoreBackupFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            try{
                val inputStream = context.contentResolver.openInputStream(uri)
                    ?: throw IOException("InputStream is null")

                val jsonString = inputStream.bufferedReader().use { it.readText() }
                val backupData = parseJsonToBackupData(jsonString)

                if (backupData != null) {
                    insertBackupDataToDatabase(backupData)
                    _backupResultState.value = BackupResultState.Success
                }
            } catch (e: Exception) {
                Timber.e(e, "Error restoring backup file")
                _backupResultState.value =
                    BackupResultState.Error(getApplication<Application>().getString(R.string.backup_error_reading_json))
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun parseJsonToBackupData(jsonData: String): BackupData? {
        return try {
            val gson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, JsonDeserializer { json, _, _ ->
                    val format = SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US)
                    format.parse(json.asString)
                })
                .create()
            gson.fromJson(jsonData, BackupData::class.java)
        } catch (e: JsonSyntaxException) {
            Timber.e(e, "Error parsing backup data")
            _backupResultState.value =
                BackupResultState.Error(getApplication<Application>().getString(R.string.backup_error_reading_json))
            null
        }
    }

    private suspend fun insertBackupDataToDatabase(backupData: BackupData) {
        backupData.categoriesWithItems.forEach { categoryWithItems ->
            dao.insertCategory(categoryWithItems.category)
            categoryWithItems.items.forEach { item ->
                Timber.d("Inserting item: $item")
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

            _backupResultState.value = BackupResultState.Success
        } catch (e: Exception) {
            _backupResultState.value =
                BackupResultState.Error(getApplication<Application>().getString(R.string.backup_save_file_error))
            Timber.e(e, "Failed to save backup file")
        }
    }
}