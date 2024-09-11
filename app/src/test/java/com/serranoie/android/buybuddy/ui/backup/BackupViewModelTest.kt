package com.serranoie.android.buybuddy.ui.backup

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.serranoie.android.buybuddy.data.backup.BackupData
import com.serranoie.android.buybuddy.data.persistance.dao.BuyBuddyDao
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryEntity
import com.serranoie.android.buybuddy.data.persistance.entity.CategoryWithItemsEntity
import com.serranoie.android.buybuddy.data.persistance.entity.ItemEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class BackupViewModelTest {

    @MockK
    private lateinit var dao: BuyBuddyDao

    @MockK
    private lateinit var context: Context

    @MockK
    private lateinit var uri: Uri

    private lateinit var viewModel: BackupViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = BackupViewModel(dao)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
        val bufferedReader = inputStream.bufferedReader()
        val contentResolver = mockk<ContentResolver>()

        every { context.contentResolver } returns contentResolver
        every { contentResolver.openInputStream(uri) } returns inputStream

        coEvery { dao.insertCategory(any()) } returns 1L
        coEvery { dao.insertItem(any()) } returns 1L

        viewModel.restoreBackupFile(context, uri)

        verify { contentResolver.openInputStream(uri) }
        coVerify { dao.insertCategory(any()) }
        coVerify { dao.insertItem(any()) }
    }

    @Test
    fun `restoreBackupFile should log error if failed to read JSON`() = runTest {
        every { context.contentResolver.openInputStream(uri) } returns null

        viewModel.restoreBackupFile(context, uri)

        verify { context.contentResolver.openInputStream(uri) }
    }
}