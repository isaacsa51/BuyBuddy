package com.serranoie.android.buybuddy.ui.backup

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.gson.Gson
import com.serranoie.android.buybuddy.data.backup.BackupData
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class BackupViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dao: BuyBuddyDao = mockk()
    private val application: Application = mockk()
    private val context: Context = mockk()
    private val uri: Uri = mockk()

    private lateinit var viewModel: BackupViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = BackupViewModel(dao, application)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test backupResultState initializes to Idle`() = runTest {
        viewModel.backupResultState.test {
            assertEquals(BackupResultState.Idle, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `generateBackupFile should get data, convert to JSON and save to file`() = runTest {
        val backupData = BackupData(
            categoriesWithItems = listOf(
                CategoryWithItemsEntity(
                    category = CategoryEntity(categoryId = 1, name = "Category"), items = listOf(
                        ItemEntity(
                            itemId = 1,
                            name = "Item",
                            categoryId = 1,
                            description = "Description",
                            usage = "Usage",
                            benefits = "Benefits",
                            disadvantages = "Disadvantages",
                            price = 10.0,
                            reminderTime = Date(),
                            reminderDate = Date(),
                            status = false
                        )
                    )
                )
            ),
            categories = listOf(CategoryEntity(categoryId = 1, name = "Category")),
            items = listOf(
                ItemEntity(
                    itemId = 1,
                    name = "Item",
                    categoryId = 1,
                    description = "Description",
                    usage = "Usage",
                    benefits = "Benefits",
                    disadvantages = "Disadvantages",
                    price = 10.0,
                    reminderTime = Date(),
                    reminderDate = Date(),
                    status = false
                )
            )
        )
        coEvery { dao.getCategoriesWithItems() } returns flowOf(backupData.categoriesWithItems)
        coEvery { dao.getAllItems() } returns flowOf(backupData.items)

        val jsonData = Gson().toJson(backupData)
        val outputStream = mockk<java.io.OutputStream>(relaxed = true)
        every { context.contentResolver.openOutputStream(uri) } returns outputStream

        viewModel.generateBackupFile(context, uri)

        coVerify { dao.getCategoriesWithItems() }
        coVerify { dao.getAllItems() }
        verify { outputStream.write(jsonData.toByteArray()) }
    }

    @Test
    fun `restoreBackupFile should read JSON, parse data and insert to database`() = runTest {
        val jsonData = """
        {
          "categories": [
            {
              "categoryId": 1,
              "name": "Ropa"
            }
          ],
          "categoriesWithItems": [
            {
              "category": {
                "categoryId": 1,
                "name": "Ropa"
              },
              "items": [
                {
                  "benefits": "kjhklhkl",
                  "categoryId": 1,
                  "description": "asdfasdft",
                  "disadvantages": "jkgjgkjhg",
                  "itemId": 1,
                  "name": "android 11",
                  "price": 152,
                  "reminderDate": "Sep 19, 2024 2:15:00 PM",
                  "reminderTime": "Sep 19, 2024 2:15:00 PM",
                  "status": false,
                  "usage": "Seguido"
                }
              ]
            }
          ],
          "items": [
            {
              "benefits": "kjhklhkl",
              "categoryId": 1,
              "description": "asdfasdf\t",
              "disadvantages": "jkgjgkjhg",
              "itemId": 1,
              "name": "android 11",
              "price": 152,
              "reminderDate": "Sep 19, 2024 2:15:00 PM",
              "reminderTime": "Sep 19, 2024 2:15:00 PM",
              "status": false,
              "usage": "Seguido"
            }
          ]
        }
        """.trimIndent()
        val inputStream = jsonData.byteInputStream()
        val contentResolver = mockk<ContentResolver>()
        every { context.contentResolver } returns contentResolver
        every { contentResolver.openInputStream(uri) } returns inputStream

        coEvery { dao.insertCategory(any()) } returns 1L
        coEvery { dao.insertItem(any()) } returns 1L

        viewModel.restoreBackupFile(context, uri)

        advanceUntilIdle()

        verify { contentResolver.openInputStream(uri) }
    }

    @Test
    fun `restoreBackupFile should log error if failed to read JSON`() = runTest {
        val mockApplication = mockk<Application>()
        val errorString = "in JSON"
        every { mockApplication.getString(any()) } returns errorString

        val mockContext = mockk<Context>()
        every { mockContext.applicationContext } returns mockApplication
        val contentResolver = mockk<ContentResolver>()
        every { mockContext.contentResolver } returns contentResolver
        every { contentResolver.openInputStream(uri) } returns null

        val viewModel = BackupViewModel(dao, mockApplication)

        viewModel.restoreBackupFile(mockContext, uri)
        advanceUntilIdle()

        assertEquals(
            BackupResultState.Error(errorString),
            viewModel.backupResultState.value
        )
    }
}